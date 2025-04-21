package com.dexatek.bluetooth

sealed class DKBlueToothException(message : String) : Exception(message) {
    class BluetoothStateError(state: ByteArray) : DKBlueToothException("Bluetooth state is not a valid")
}