package com.dexatek.bluetooth

import com.dexatek.bluetooth.data.DKBlueToothData
import com.dexatek.bluetooth.data.DKBlueToothWIFIData
import com.dexatek.bluetooth.data.DKBlueToothPairingState
import com.dexatek.bluetooth.data.DKWiredNetworkData
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
interface DKBlueToothRepository {

    /**
     * Start discovering nearby devices.
     * @return [Flow] A stream of discovered devices.
     *
     * Note: Blocking thread
     *
     */
    fun startDiscovery() : Flow<DataStatus<DKBlueToothData>>

    /**
     * Stop discovering nearby devices.
     * @return [Flow] stop discovery status.
     */
    fun stopDiscovery()

    /**
     * Connect to a GATT server.
     * To disconnect, call [disconnect] or cancel the flow.
     * @return [Flow] A stream of GATT server, null if failed to connect.
     */
    fun connect(blueToothAddress: String) : Flow<DataStatus<Unit>>

    /**
     * Disconnect from a GATT server.
     */
    fun disconnect(blueToothAddress: String)
    
    fun readState(blueToothAddress: String): Flow<DataStatus<DKBlueToothPairingState>>

    fun setDomain(blueToothAddress: String, domain: String): Flow<DataStatus<Unit>>

    /**
     * Read the list of WiFi networks.
     * If the device is not connected, it will automatically connect and disconnect after completing the read operation.
     * @param blueToothAddress The bluetooth address.
     * @return [Flow] A stream of WiFi data.
     */
    fun readWifiList(blueToothAddress: String): Flow<DataStatus<DKBlueToothWIFIData>>

    /**
     * Set the WiFi network.
     * @param dkBlueToothWIFIData The WiFi data.
     * @param password The WiFi password.
     */
    fun setWifi(dkBlueToothWIFIData: DKBlueToothWIFIData, password: String) : Flow<DataStatus<Unit>>

    /**
     * Set the OTP token.
     * @param blueToothAddress The bluetooth address.
     * @param deviceName The device name.
     */
    fun setOTPToken(blueToothAddress: String, deviceName: String, optToken: String) : Flow<DataStatus<Unit>>

    /**
     * Get the MAC address.
     * @param blueToothAddress The bluetooth address.
     * @return [Flow] A stream of MAC address.
     */
    fun readMacAddress(blueToothAddress: String): Flow<DataStatus<ByteArray>>

    fun readWiredNetwork(blueToothAddress: String): Flow<DataStatus<DKWiredNetworkData>>
    fun setStaticIpInWiredNetwork(blueToothAddress: String, ipAddress: String, maskAddress: String, routerAddress: String) : Flow<DataStatus<Unit>>
    fun setDynamicIpInWiredNetwork(blueToothAddress: String) : Flow<DataStatus<Unit>>

}
