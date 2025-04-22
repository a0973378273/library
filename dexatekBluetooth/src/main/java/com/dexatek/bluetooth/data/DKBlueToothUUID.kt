package com.dexatek.bluetooth.data

enum class DKBlueToothUUID(val UUID:String) {
    SERVER("0D27FA90-F0D4-469D-AFD3-605A6EBBDB13"),
    DOMAIN("0d27fc00-f0d4-469d-afd3-605a6ebbdb13"),
    /**
     * Properties: READ
     *
     * Definition uint8_t array, 8 bytes The MAC address of the device and last two bytes are its CRC16. Read only.
     *
     */
    MAC_ADDRESS("0D27FB92-F0D4-469D-AFD3-605A6EBBDB13"),

    /**
     * Properties: READ, WRITE
     *
     * Definition uint8_t, 1 bytes The APP can get the current BLE pairing state of the device by reading this characteristic.
     *
     * Write BLE_PAIRING_MANAGER_STATE_WIFI_SCANNING to this characteristic to start WiFi scanning on the device.
     */
    STATE("0D27FB93-F0D4-469D-AFD3-605A6EBBDB13"),

    /**
     * Properties: WRITE
     *
     * Definition uint8_t array, 20 bytes
     *
     * Length (1-byte)	Sequence (1-byte)	Segmented WiFi credential (18-bytes)
     *
     * Segmented WiFi credential:
     *
     * Security_type(2-byte)	SSID len(2-byte)	Password len(2-byte)	SSID(~bytes)	Password(~bytes)	CRC16(2-byte)
     *
     * Security type: uint16_t, 2 bytes, Little-Endian. The security type setting of a WiFi AP.
     *
     * NOTE: For the normal case, the App writes the security value to the device according to the WiFi scanning result. For the hidden/customized SSID case, the App can't get security type by WiFi scanning. Therefore, the App writes 0xFFFF then the device will try to connect to the WiFi AP with possible security types. If the device still can't establish the connection with WiFi AP after trying all security types in the list, the status will be set to BLE_PAIRING_MANAGER_STATE_ERROR_WIFI_CONNECT_FAIL.
     *
     * SSID len: uint16_t, 2 bytes, Little-Endian. The SSID length of a WiFi AP.
     *
     * Password len: uint16_t, 2 bytes, Little-Endian. The password length of a WiFi AP.
     *
     * SSID: char, variant bytes, Big-Endian. The selected SSID to connect.
     *
     * Password: char, variant bytes, Big-Endian. The user input password.
     *
     * CRC16: uint16_t, 2 bytes, Little-Endian. The CRC16 of all bytes before these 2 bytes. Please refer to CRC16 calculation.
     */
    WIFI_CREDENTIAL("0D27FB95-F0D4-469D-AFD3-605A6EBBDB13"),
    /**
     * Properties: WRITE
     *
     * Definition char array, 20 bytes, Big-Endian Because of the size of AWS certificates is too large for BLE transmission. Therefore, the APP needs to acquire a token for allowing the device to download the AWS certificates from API server directly.
     *
     *
     */
    OTP_TOKEN("0D27FB96-F0D4-469D-AFD3-605A6EBBDB13"),

    /**
     * Properties: READ
     *
     * Definition uint8_t array, 20 bytes The APP can get the WiFi scanning results by reading this characteristic repeatedly after the device reports the state is BLE_PAIRING_MANAGER_STATE_WIFI_SCAN_DONE.
     *
     * There might be multiple WiFi APs scanned by the device. The APP has to read this characteristic with multiple times to obtain full WiFi scanning results. The device changes the content of the characteristic after every read.
     *
     * Considering the user's phone may only support BLE4.0 or below, The payload length is up to 20 bytes. Therefore, every single WiFi scanning result is further divided into multiple segments.
     *
     * Length (1-byte)	Index (1-byte)	Sequence (1-byte)	Segmented WiFi scanning result (17-bytes)
     *
     * MAC address(6-byte)	Chanel(2-byte)	RSSI(2-byte)	Band(2-byte)	Security_type(2-byte)	SSID len(2-byte)	SSID(~bytes)	CRC16(2-byte)
     *
     * MAC address: uint8_t array, 6 bytes, Big-Endian. The MAC address of a WiFi AP
     *
     * Chanel: uint16_t, 2 bytes, Little-Endian. The channel setting of a WiFi AP.
     *
     * RSSI: int16_t, 2 bytes, Little-Endian. The RSSI value of a WiFi AP.
     *
     * Band: uint16_t, 2 bytes, Little-Endian. The band setting of a WiFi AP. 0: 2.4G 1: 5G
     *
     * Security type: uint16_t, 2 bytes, Little-Endian. The security type setting of a WiFi AP.
     *
     * SSID len: uint16_t, 2 bytes, Little-Endian. The SSID length of a WiFi AP.
     *
     * SSID: char, variant bytes, Big-Endian. The SSID of a WiFi AP. P.S.: 中文SSID用python tool測試亦可成功連線.
     *
     * CRC16: uint16_t, 2 bytes, Little-Endian. The CRC16 of all bytes before these 2 bytes. Please refer to CRC16 calculation.
     */
    WIFI_SCANNING_RESULTS("0D27FB94-F0D4-469D-AFD3-605A6EBBDB13"),

    WIRED_NETWORK_CONFIG("0D27FB97-F0D4-469D-AFD3-605A6EBBDB13")

}