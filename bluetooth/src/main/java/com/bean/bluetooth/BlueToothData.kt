package com.bean.bluetooth

import android.bluetooth.BluetoothDevice

data class BlueToothData(val address: String, val rssi: Int, val device: BluetoothDevice, val record: ByteArray? = null)
