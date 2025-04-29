package com.dexatek.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID

class BlueToothDataSourceImpl(private val context: Context) : BlueToothDataSource {
    private var stopDiscovery: (() -> Unit)? = null
    private var read: ((ByteArray) -> Unit)? = null
    private var stopReadWrite: MutableList<(() -> Unit)> = mutableListOf()
    private var disconnect: (() -> Unit)? = null
    private var write: ((Boolean) -> Unit)? = null

    override fun startDiscovery() = callbackFlow {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            throw BlueToothException.BlueToothPermissionDenied()
        }
        val scanCallback = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                super.onScanResult(callbackType, result)
                result.apply {
                    trySend(BlueToothData(device.address, rssi, device, scanRecord?.bytes))
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                super.onBatchScanResults(results)
                for (result in results) {
                    Log.d("BLE_SCAN", "批量掃描結果: ${result.device.address}")
                }
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.e("BLE_SCAN", "掃描失敗，錯誤碼: $errorCode")
            }
        }
        bluetoothLeScanner.startScan(scanCallback)

        stopDiscovery = { close() }

        awaitClose {
            bluetoothLeScanner.stopScan(scanCallback)
            stopDiscovery = null
        }
    }

    override fun stopDiscovery() { stopDiscovery?.run { invoke() } }

    override fun connectGatt(device: BluetoothDevice) = callbackFlow {
        val gatt = device.connectGatt(context, false, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
                super.onConnectionStateChange(gatt, status, newState)
                when (newState) {
                    BluetoothGatt.STATE_DISCONNECTED -> {
                        close(BlueToothException.BlueToothDisconnect())
                    }

                    BluetoothGatt.STATE_CONNECTED -> {
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.BLUETOOTH_CONNECT
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            gatt?.discoverServices()
                        } else {
                            close(BlueToothException.BlueToothPermissionDenied())
                        }
                    }
                }
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                super.onServicesDiscovered(gatt, status)
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    if (gatt != null)
                        trySend(gatt)
                } else {
                    close(BlueToothException.BlueToothDisconnect())
                }
            }

            override fun onCharacteristicRead(
                gatt: BluetoothGatt,
                characteristic: BluetoothGattCharacteristic,
                value: ByteArray,
                status: Int
            ) {
                super.onCharacteristicRead(gatt, characteristic, value, status)
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    read?.invoke(value)
                }
            }

            override fun onCharacteristicWrite(
                gatt: BluetoothGatt?,
                characteristic: BluetoothGattCharacteristic?,
                status: Int
            ) {
                super.onCharacteristicWrite(gatt, characteristic, status)
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    write?.invoke(true)
                } else {
                    write?.invoke(false)
                }
            }
        })
        disconnect = {
            close()
        }
        awaitClose {
            stopReadWrite.forEach { it.invoke() }
            gatt.disconnect()
            gatt.close()
        }
    }

    override fun disconnectGatt(device: BluetoothDevice) {
        disconnect?.invoke()
    }

    override fun writeGatt(
        gatt: BluetoothGatt,
        serverUUID: String,
        characteristicUUID: String,
        vararg values: ByteArray,
        repeatTime: Long
    ) = callbackFlow {
        val service = gatt.getService(UUID.fromString(serverUUID))
        val characteristic = service?.getCharacteristic(UUID.fromString(characteristicUUID))
        var index = 0

        val stop = {
            close()
            Unit
        }
        stopReadWrite.add(stop)

        write = {
            if (it) {
                index++
                if (index >= values.size) {
                    trySend(Unit)
                    close()
                } else {
                    try {
                        if (characteristic == null) {
                            close(BlueToothException.BlueToothCommandNotFound())
                        } else {
                            writeGatt(gatt, characteristic, values[index])
                        }
                    } catch (e: Exception) {
                        close(e)
                    }
                }
            } else {
                close(BlueToothException.BlueToothWriteFailed())
            }
        }
        try {
            if (characteristic == null) {
                close(BlueToothException.BlueToothCommandNotFound())
            } else {
                writeGatt(gatt, characteristic, values[index])
            }
        } catch (e: Exception) {
            close(e)
        }

        awaitClose {
            write = null
            stopReadWrite.remove(stop)
        }
    }

    private fun writeGatt(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                if (VERSION.SDK_INT <= 33) {
                    characteristic.value = value
                    gatt.writeCharacteristic(characteristic)
                } else {
                    gatt.writeCharacteristic(characteristic, value, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
                }
            } else {
                throw BlueToothException.BlueToothPermissionDenied()
            }
        }

    private var readJob: Job? = null
    override fun readGatt(
        gatt: BluetoothGatt,
        serverUUID: String,
        characteristicUUID: String,
        repeatTime: Long
    ) = callbackFlow {
        val stop = {
            close()
            Unit
        }
        stopReadWrite.add(stop)

        readJob = launch {
            while (isActive) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    close(BlueToothException.BlueToothPermissionDenied())
                    break
                }
                gatt.getService(UUID.fromString(serverUUID))?.apply {
                    getCharacteristic(UUID.fromString(characteristicUUID))?.let {
                        gatt.readCharacteristic(it)
                        delay(repeatTime)
                    } ?: run {
                        close(IOException("Characteristic not found!"))
                    }
                } ?: run {
                    close(IOException("Service not found!"))
                }
            }
        }
        read = {
            trySend(it)
        }
        awaitClose {
            Log.d(javaClass.name, "Read gatt is closed")
            read = null
            stopReadWrite.remove(stop)
            readJob?.cancel()
        }
    }
}