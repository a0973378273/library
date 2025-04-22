package com.bean.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import com.dexatek.bluetooth.tool.getDataStatusByFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class BlueToothRepositoryImpl(private val blueToothDataSource: BlueToothDataSource) :
    BlueToothRepository {
    private val repeatTime = 200L

    private var bluetoothDeviceDataList = mutableListOf<BlueToothData>()
    private fun getDeviceData(blueToothAddress: String): BlueToothData {
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
                .onEach { blueToothDeviceData -> bluetoothDeviceDataList.add(blueToothDeviceData) }
        }

    override fun stopDiscovery() {
        blueToothDataSource.stopDiscovery()
    }

    override fun connect(blueToothAddress: String) =
        getDataStatusByFlow {
            flow { emit(getDevice(blueToothAddress)) }
                .flatMapConcat {
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

    override fun write(blueToothAddress: String, serverUUID: String, characteristicUUID: String, vararg value: ByteArray) =
        getDataStatusByFlow {
            flow { emit(getGatt(blueToothAddress)) }
                .flatMapConcat {
                    blueToothDataSource.writeGatt(
                        it,
                        serverUUID,
                        characteristicUUID,
                        *value,
                        repeatTime = repeatTime
                    )
                }
        }

    override fun read(blueToothAddress: String, serverUUID: String, characteristicUUID: String) =
        getDataStatusByFlow {
            flow { emit(getGatt(blueToothAddress)) }
                .flatMapConcat {
                    blueToothDataSource.readGatt(
                        it,
                        serverUUID,
                        characteristicUUID,
                        repeatTime
                    )
                }
        }
}