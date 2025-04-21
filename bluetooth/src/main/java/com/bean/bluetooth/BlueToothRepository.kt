package com.bean.bluetooth

import com.dexatek.bluetooth.tool.DataStatus
import kotlinx.coroutines.flow.Flow

/**
 * A repository to interact with Bluetooth devices.
 *
 * Note: Add the following permissions to your AndroidManifest.xml file
 *     <uses-permission android:name="android.permission.BLUETOOTH" />
 *     <uses-permission android:name="android.permission.BLUETOOTH_SCAN"/>
 *     <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
 *     <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 *     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 *     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 */
interface BlueToothRepository {
    /**
     * Start discovering nearby devices.
     * @param discoveryTime The time to discover devices. default is null.
     * @return [Flow] A stream of discovered devices.
     *
     * Note: Blocking thread
     */
    fun startDiscovery() : Flow<DataStatus<BlueToothData>>

    /**
     * Stop discovering nearby devices.
     * @return [Flow] stop discovery status.
     */
    fun stopDiscovery()

    /**
     * Connect to a GATT server.
     * To disconnect, call [disconnect] or cancel the flow.
     * @param device The device to connect.
     * @return [Flow] A stream of GATT server
     */
    fun connect(blueToothAddress: String) : Flow<DataStatus<Unit>>

    /**
     * Disconnect from a GATT server.
     */
    fun disconnect(blueToothAddress: String)

    /**
     * Write data to a GATT characteristic.
     * @param gatt The GATT server.
     * @param serverUUID The server UUID.
     * @param characteristicUUID The characteristic UUID.
     * @param value The data to write.
     * @return [Flow] A stream of write status.
     */
    fun write(blueToothAddress: String, serverUUID: String, characteristicUUID: String, vararg value: ByteArray): Flow<DataStatus<Unit>>

    /**
     * Read data from a GATT characteristic.
     * @param gatt The GATT server.
     * @param serverUUID The server UUID.
     * @param characteristicUUID The characteristic UUID.
     * @param repeatTime The time to repeat reading. default is 1000.
     * @return [Flow] A stream of read data.
     */
    fun read(blueToothAddress: String, serverUUID: String, characteristicUUID: String): Flow<DataStatus<ByteArray>>

}