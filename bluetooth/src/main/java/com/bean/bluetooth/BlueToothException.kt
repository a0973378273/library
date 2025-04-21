package com.bean.bluetooth


sealed class BlueToothException(message : String) : Exception(message) {
    class BlueToothNotFound: BlueToothException("Bluetooth is not found")
    class BlueToothStateNotDefined : BlueToothException("Bluetooth state is not defined")
    class BlueToothDisconnect: BlueToothException("Bluetooth is disconnected")
    class BlueToothWriteFailed: BlueToothException("Bluetooth write failed")
    class BlueToothCommandNotFound: BlueToothException("Bluetooth command is not found")
    class BlueToothPermissionDenied: BlueToothException("Bluetooth permission is denied")

    class BlueToothWIFIConnectFailed: BlueToothException("WIFI connect failed")
}