package com.dexatek.bluetooth.data

enum class DKBlueToothPairingState(val value: Byte) {
    UNINIT(0),
    INIT(1),
    CONNECTED(2),
    DISCONNECT(3),
    WIFI_SCANNING(4),
    WIFI_SCAN_DONE(5),
    WIFI_CREDENTIAL_RECEIVED(6),
    WIFI_CONNECTED(7),
    AWS_CONNECTING(8),
    AWS_CONNECTED(9),
    CLOSE(10),

    ERROR_SCAN(11),
    ERROR_WIFI_CREDENTIAL_FAIL(12),
    ERROR_WIFI_CONNECT_FAIL(13),
    ERROR_AWS_FAIL(14),
    ERROR_API_SERVER_FAIL(15);

    companion object {
        fun fromValue(value: Byte): DKBlueToothPairingState? {
            return entries.find { it.value == value }
        }
    }
}