package com.dexatek.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.util.Log
import com.bean.bluetooth.BlueToothDataSource
import com.bean.bluetooth.BlueToothException
import com.dexatek.bluetooth.data.DKBlueToothData
import com.dexatek.bluetooth.data.DKBlueToothWIFIData
import com.dexatek.bluetooth.data.DKBlueToothPairingState
import com.dexatek.bluetooth.data.DKBlueToothUUID
import com.dexatek.bluetooth.data.DKWiredNetworkData
import com.dexatek.bluetooth.tool.DataStatus
import com.dexatek.bluetooth.tool.getDataStatusByFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.withIndex

class DKBlueToothRepositoryImpl(private val blueToothDataSource: BlueToothDataSource) :
    DKBlueToothRepository {
    private val repeatTime = 200L
    // 讀取逾期時間
    private val WRITE_READ_TIME_OUT = 60000
    // 寫入之後停幾秒才做讀取的動作
    private val WRITE_READ_DELAY = 2000L
    // 防止在時間內有重複讀取的值
    private val DEBOUNCE_TIME = 1000L

    private var bluetoothDeviceDataList = mutableListOf<DKBlueToothData>()
    private fun getDeviceData(blueToothAddress: String): DKBlueToothData {
        return bluetoothDeviceDataList.firstOrNull { it.address == blueToothAddress } ?: throw BlueToothException.BlueToothNotFound()
    }

    private fun getDevice(blueToothAddress: String): BluetoothDevice {
        return bluetoothDeviceDataList.map { it.device }.firstOrNull { it.address == blueToothAddress } ?: throw BlueToothException.BlueToothNotFound()
    }

    private var bluetoothGattList = mutableListOf<BluetoothGatt>()
    private fun getGatt(blueToothAddress: String): BluetoothGatt {
        return bluetoothGattList.firstOrNull { it.device.address == blueToothAddress } ?: throw BlueToothException.BlueToothDisconnect()
    }

    private fun getGattOrNull(blueToothAddress: String): BluetoothGatt? {
        return bluetoothGattList.firstOrNull { it.device.address == blueToothAddress }
    }

    override fun startDiscovery() =
        getDataStatusByFlow {
            blueToothDataSource.startDiscovery()
                .filter { bluetoothDeviceDataList.none { device -> device.address == it.address } } // 移除重複
                .filter { it.record != null }
                .map { DKBlueToothData.from(it.address, it.rssi, it.device, it.record!!) } // 這邊要 copy 一下，因為會有 reference 的問題
                .onEach { blueToothDeviceData -> bluetoothDeviceDataList.add(blueToothDeviceData) }
        }

    override fun stopDiscovery() {
        blueToothDataSource.stopDiscovery()
    }

    override fun connect(blueToothAddress: String) =
        getDataStatusByFlow {
            flow {
                emit(getDevice(blueToothAddress))
            }.flatMapConcat {
                blueToothDataSource.connectGatt(it).map { gatt ->
                    bluetoothGattList.add(gatt)
                    Unit
                }
            }
        }

    override fun disconnect(blueToothAddress: String) {
        getGattOrNull(blueToothAddress)?.let {
            blueToothDataSource.disconnectGatt(it.device)
            bluetoothGattList.remove(it)
        }
    }

    override fun readState(blueToothAddress: String): Flow<DataStatus<DKBlueToothPairingState>> {
        return getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.flatMapConcat {
                blueToothDataSource.readGatt(it, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.STATE.UUID, repeatTime)
            }.map {
                DKBlueToothPairingState.fromValue(it[0]) ?: throw BlueToothException.BlueToothStateNotDefined()
            }
        }
    }

    override fun setDomain(blueToothAddress: String, domain: String): Flow<DataStatus<Unit>> {
        return getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.debounce(DEBOUNCE_TIME).flatMapConcat { gatt ->
                blueToothDataSource.writeGatt(gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.DOMAIN.UUID, domain.toByteArray(), repeatTime = repeatTime)
            }
        }
    }

    override fun readWifiList(blueToothAddress: String): Flow<DataStatus<DKBlueToothWIFIData>> {
        return getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.debounce(DEBOUNCE_TIME).flatMapConcat { gatt ->
                blueToothDataSource.writeGatt(gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.STATE.UUID, byteArrayOf(0x04), repeatTime = repeatTime)
                    .flatMapConcat {
                        flow {
                            var buffer: ByteArray? = null
                            var stop = false
                            blueToothDataSource.readGatt(gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.WIFI_SCANNING_RESULTS.UUID, repeatTime)
                                .filter { it.size >= 3 }
                                .takeWhile { !stop } // index 1 的最高位元為 1 表示後續為重複資料
                                .collect { data ->
                                    val expectedLength = data[0].toInt() and 0xFF
                                    if (data[0] == 0x14.toByte() && buffer == null) {
                                        buffer = data.copyOfRange(3, expectedLength)
                                    } else if (buffer != null) {
                                        buffer = buffer!! + data.copyOfRange(3, expectedLength)
                                    }
                                    if (buffer != null && ((data[2].toInt() and 0x80) != 0)) {
                                        emit(buffer!!)
                                        buffer = null
                                    }

                                    stop = ((data[1].toInt() and 0x80) != 0) && ((data[2].toInt() and 0x80) != 0)
                                }
                        }
                    }
                    .map { DKBlueToothWIFIData.fromByteArray(it, blueToothAddress) }
            }
        }
    }

    override fun setWifi(dkBlueToothWIFIData: DKBlueToothWIFIData, password: String): Flow<DataStatus<Unit>> {
        val newDKBlueToothWIFIData = dkBlueToothWIFIData.setPassword(password)
        return getDataStatusByFlow {
            flow { emit(getGatt(dkBlueToothWIFIData.bluetoothAddress)) }
                .debounce(DEBOUNCE_TIME)
                .flatMapConcat { gatt ->
                    blueToothDataSource.writeGatt(
                        gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.WIFI_CREDENTIAL.UUID,
                        *newDKBlueToothWIFIData.toWIFICredential().toTypedArray(),
                        repeatTime = repeatTime
                    )
                        .flatMapConcat {
                            delay(WRITE_READ_DELAY)
                            blueToothDataSource.readGatt(gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.STATE.UUID, repeatTime)
                        }
                        .withIndex()
                        .onEach {
                            when (DKBlueToothPairingState.fromValue(it.value[0])) {
                                DKBlueToothPairingState.WIFI_SCAN_DONE -> {
                                    Log.d(javaClass.name, "setWifi: ${DKBlueToothPairingState.WIFI_SCAN_DONE.name}")
                                    if (it.index * repeatTime > WRITE_READ_TIME_OUT) throw BlueToothException.BlueToothWriteFailed() // 寫入之後，狀態超過時間沒改變，視為寫入失敗
                                }

                                DKBlueToothPairingState.WIFI_CREDENTIAL_RECEIVED -> {
                                    Log.d(javaClass.name, "setWifi: ${DKBlueToothPairingState.WIFI_CREDENTIAL_RECEIVED.name}")
                                    if (it.index * repeatTime > WRITE_READ_TIME_OUT) throw BlueToothException.BlueToothWriteFailed()
                                }

                                DKBlueToothPairingState.WIFI_CONNECTED -> {
                                    Log.d(javaClass.name, "setWifi: ${DKBlueToothPairingState.WIFI_CONNECTED.name}")
                                }

                                DKBlueToothPairingState.ERROR_WIFI_CREDENTIAL_FAIL -> {
                                    Log.d(javaClass.name, "setWifi: ${DKBlueToothPairingState.ERROR_WIFI_CREDENTIAL_FAIL.name}")
                                    throw BlueToothException.BlueToothWIFIConnectFailed()
                                }

                                DKBlueToothPairingState.ERROR_WIFI_CONNECT_FAIL -> {
                                    Log.d(javaClass.name, "setWifi: ${DKBlueToothPairingState.ERROR_WIFI_CONNECT_FAIL.name}")
                                    throw BlueToothException.BlueToothWIFIConnectFailed()
                                }

                                else -> throw DKBlueToothException.BluetoothStateError(it.value)
                            }
                        }
                        .filter { DKBlueToothPairingState.fromValue(it.value[0]) == DKBlueToothPairingState.WIFI_CONNECTED }
                        .take(1).map { }
                }
        }
    }

    override fun setOTPToken(blueToothAddress: String, deviceName: String, optToken: String): Flow<DataStatus<Unit>> {
        return getDataStatusByFlow {
            flow { emit(getGatt(blueToothAddress)) }
                .debounce(DEBOUNCE_TIME)
                .flatMapConcat { gatt ->
                    blueToothDataSource.writeGatt(
                        gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.OTP_TOKEN.UUID, optToken.toByteArray(), repeatTime = repeatTime
                    ).flatMapConcat {
                        delay(WRITE_READ_DELAY)
                        blueToothDataSource.readGatt(
                            gatt, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.STATE.UUID, repeatTime
                        )
                    }.withIndex().onEach {
                        when (DKBlueToothPairingState.fromValue(it.value[0])) {
                            DKBlueToothPairingState.WIFI_CONNECTED -> {
                                Log.d(javaClass.name, "setOTPToken: ${DKBlueToothPairingState.WIFI_CONNECTED.name}")
                                if (it.index * repeatTime > WRITE_READ_TIME_OUT) throw BlueToothException.BlueToothWriteFailed() // 寫入之後，狀態超過時間沒改變，視為寫入失敗
                            }

                            DKBlueToothPairingState.AWS_CONNECTING -> {
                                Log.d(javaClass.name, "setOTPToken: ${DKBlueToothPairingState.AWS_CONNECTING.name}")
                            }

                            DKBlueToothPairingState.AWS_CONNECTED -> {
                                Log.d(javaClass.name, "setOTPToken: ${DKBlueToothPairingState.AWS_CONNECTED.name}")
                            }

                            DKBlueToothPairingState.ERROR_WIFI_CREDENTIAL_FAIL -> {
                                Log.d(javaClass.name, "setOTPToken: ${DKBlueToothPairingState.ERROR_WIFI_CREDENTIAL_FAIL.name}")
                                throw BlueToothException.BlueToothWIFIConnectFailed()
                            }

                            DKBlueToothPairingState.ERROR_WIFI_CONNECT_FAIL -> {
                                Log.d(javaClass.name, "setOTPToken: ${DKBlueToothPairingState.ERROR_WIFI_CONNECT_FAIL.name}")
                                throw BlueToothException.BlueToothWIFIConnectFailed()
                            }

                            else -> throw DKBlueToothException.BluetoothStateError(it.value)
                        }
                    }.filter {
                        DKBlueToothPairingState.fromValue(it.value[0]) == DKBlueToothPairingState.AWS_CONNECTED
                    }.take(1).map { }
                }
        }
    }

    override fun readMacAddress(blueToothAddress: String): Flow<DataStatus<ByteArray>> =
        getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.flatMapConcat {
                blueToothDataSource.readGatt(it, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.MAC_ADDRESS.UUID, repeatTime = repeatTime)
                    .take(1)
                    .map { it.dropLast(2).toByteArray() }
            }
        }

    override fun readWiredNetwork(blueToothAddress: String): Flow<DataStatus<DKWiredNetworkData>> =
        getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.flatMapConcat {
                blueToothDataSource.readGatt(it, DKBlueToothUUID.SERVER.UUID, DKBlueToothUUID.WIRED_NETWORK_CONFIG.UUID, repeatTime = repeatTime)
                    .take(1)
                    .map { DKWiredNetworkData.from(it) }
            }
        }


    override fun setStaticIpInWiredNetwork(blueToothAddress: String, ipAddress: String, maskAddress: String, routerAddress: String): Flow<DataStatus<Unit>> =
        getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.debounce(DEBOUNCE_TIME).flatMapConcat { gatt ->
                val ipBytes = ipAddress.split(".").map { it.toInt().toByte() }
                val routerBytes = routerAddress.split(".").map { it.toInt().toByte() }
                val maskBytes = maskAddress.split(".").map { it.toInt().toByte() }
                val byteArray = byteArrayOf(0x00) + (ipBytes + routerBytes + maskBytes).toByteArray()
                blueToothDataSource.writeGatt(
                    gatt,
                    DKBlueToothUUID.SERVER.UUID,
                    DKBlueToothUUID.WIRED_NETWORK_CONFIG.UUID,
                    byteArray,
                    repeatTime = repeatTime
                )
            }
        }


    override fun setDynamicIpInWiredNetwork(blueToothAddress: String): Flow<DataStatus<Unit>> =
        getDataStatusByFlow {
            flow {
                emit(getGatt(blueToothAddress))
            }.debounce(DEBOUNCE_TIME).flatMapConcat { gatt ->
                val byteArray = byteArrayOf(0x02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                blueToothDataSource.writeGatt(
                    gatt,
                    DKBlueToothUUID.SERVER.UUID,
                    DKBlueToothUUID.WIRED_NETWORK_CONFIG.UUID,
                    byteArray,
                    repeatTime = repeatTime
                )
            }
        }

}