package com.dexatek.bluetooth.data

import android.bluetooth.BluetoothDevice

data class DKBlueToothData(
    val name: String,
    val address: String,
    val rssi: Int,                              // 接收信號強度
    val device: BluetoothDevice,
    val flags: UByte? = null,                  // Flags (可選)
    val companyId: UShort? = null,              // 廠商 ID
    val devicePlatform: UByte? = null,         // 設備平台
    val firmwareId: UByte? = null,             // 韌體版本
    val deviceType: UByte? = null,             // 設備類型
    val crc: UByte? = null,                    // 校驗碼
) {
    companion object {
        fun from(address: String, rssi: Int, device: BluetoothDevice, data: ByteArray): DKBlueToothData {
            var index = 0
            var flags: UByte? = null
            var companyId: UShort? = null
            var devicePlatform: UByte? = null
            var firmwareId: UByte? = null
            var deviceType: UByte? =  null
            var crc: UByte? = null
            var localName: String? = null

            while (index < data.size) {
                val length = data[index].toInt()
                if (length == 0) break
                val type = data[index + 1].toInt() and 0xFF
                val content = data.copyOfRange(index + 2, index + 1 + length)

                when (type) {
                    0x01 -> { // Flags
                        flags = content[0].toUByte()
                    }
                    0xFF -> {
                        if (content.size > 1)
                        companyId = ((content[0].toInt() and 0xFF) shl 8 or (content[1].toInt() and 0xFF)).toUShort()
                        if (content.size > 2)
                        devicePlatform = content[2].toUByte()
                        if (content.size > 3)
                        firmwareId = content[3].toUByte()
                        if (content.size > 4)
                        deviceType = content[4].toUByte()
                        if (content.size > 5)
                        crc = content[5].toUByte()
                    }
                    0x09 -> { // Local Name
                        localName = content.toString(Charsets.UTF_8)
                    }
                }

                index += (1 + length)
            }

            // 預設 name 和 address 為空字串，state 為預設值，依需求修改
            return DKBlueToothData(
                address = address,
                rssi = rssi,
                device = device, // 根據你的定義替換此預設值
                flags = flags,
                companyId = companyId,
                devicePlatform = devicePlatform,
                firmwareId = firmwareId,
                deviceType = deviceType,
                crc = crc,
                name = localName?:""
            )
        }
    }
}