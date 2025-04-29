package com.dexatek.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import kotlinx.coroutines.flow.Flow

interface BlueToothDataSource {
    /**
     * Start discovery for Bluetooth devices.
     * @param discoveryTime The time to discover devices, in seconds. If null, use default time.
     * @return [Flow] A stream of discovered devices.
     */
    fun startDiscovery(): Flow<BlueToothData>

    /**
     * Stop discovery for Bluetooth devices.
     */
    fun stopDiscovery()

    /**
     * Connect to a GATT server.
     * @param device The device to connect.
     * @return [Flow] A stream of GATT server, null if failed to connect.
     */
    fun connectGatt(device: BluetoothDevice): Flow<BluetoothGatt>

    /**
     * Disconnect from a GATT server.
     * @param device The device to disconnect.
     */
    fun disconnectGatt(device: BluetoothDevice)

    /**
     * Write data to a GATT characteristic.
     * @param gatt The GATT server.
     * @param serverUUID The server UUID.
     * @param characteristicUUID The characteristic UUID.
     * @param values The value to write.
     * @return [Flow] A stream of write status, if success, exception if failed.
     * @throws BlueToothException.BlueToothPermissionDenied, if permission is denied.
     * @throws BlueToothException.BlueToothWriteFailed, if failed to write.
     */
    fun writeGatt(gatt: BluetoothGatt, serverUUID: String, characteristicUUID: String,vararg values: ByteArray, repeatTime: Long = 200): Flow<Unit>

    /**
     * Read data from a GATT characteristic.
     * @param gatt The GATT server.
     * @param serverUUID The server UUID.
     * @param characteristicUUID The characteristic UUID.
     * @param repeatTime The time to repeat reading. default is 200.
     */
    fun readGatt(gatt: BluetoothGatt, serverUUID: String, characteristicUUID: String, repeatTime: Long = 200): Flow<ByteArray>
}