package com.dexatek.bluetooth.data

import com.dexatek.bluetooth.CRC
import java.nio.ByteBuffer
import java.nio.ByteOrder

data class DKBlueToothWIFIData @OptIn(ExperimentalUnsignedTypes::class) constructor(
    val macAddress: UByteArray,              // 6 bytes, Big-Endian
    val channel: UShort,                     // 2 bytes, Little-Endian
    val rssi: Short,                         // 2 bytes, Little-Endian
    val band: UShort,                        // 2 bytes, Little-Endian
    val securityType: UShort,                // 2 bytes, Little-Endian
    val ssidLength: UShort,                  // 2 bytes, Little-Endian
    val passwordLength: UShort? = null,      // 2 bytes, Little-Endian
    val ssid: ByteArray,                     // SSID variable length
    val password: ByteArray? = null,         // password variable length
    val crc16: UShort,                       // 2 bytes, CRC16
    val bluetoothAddress: String             // Bluetooth address
){
    @OptIn(ExperimentalUnsignedTypes::class)
    fun getMacAddressHex(): String {
        return macAddress.joinToString(":") { it.toString(16).padStart(2, '0').uppercase() }
    }

    fun getSsid(): String {
        return String(ssid, Charsets.UTF_8)
    }

    fun setPassword(password: String): DKBlueToothWIFIData {
        val passwordBytes = password.toByteArray(Charsets.UTF_8)
        val passwordLength = passwordBytes.size.toUShort()
        return this.copy(password = passwordBytes, passwordLength = passwordLength)
    }

    fun toWIFICredential() : List<ByteArray> {
        var credential = ByteBuffer.allocate(6 + ssid.size + password!!.size).apply {
            order(ByteOrder.LITTLE_ENDIAN)
            putShort(securityType.toShort())
            putShort(ssidLength.toShort())
            putShort(passwordLength!!.toShort())
            order(ByteOrder.BIG_ENDIAN)
            put(ssid)
            put(password)
        }.array()
        val crc = (CRC.crc16CCITT_Kermit(credential))
        val crcByteArray = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((crc and 0xFFFF).toShort()).array()
        println("crc = ${crc.toString(16)}")
        println("crcByteArray = ${crcByteArray.joinToString { it.toString(16) }}")
        credential += crcByteArray
        println("credential = ${credential.joinToString { it.toString(16) }}")
        // 補上 Length, Index, 確保每一個 ByteArray 都是20 bytes
        return credential
            .toList()
            .chunked(18)
            .mapIndexed { index, chunk ->
                val lengthByte = (chunk.size + 2).toByte()
                var indexByte = index.toByte()

                val isLast = index == ((credential.size - 1) / 18) // 判斷是否是最後一組

                if (isLast) {
                    indexByte = (indexByte.toInt() or 0x80).toByte() // 設置最高位
                }
                //補齊18 bytes
                val chunk18Byte = if (chunk.size < 18) {
                    chunk.toByteArray() + ByteArray(18 - chunk.size)
                } else {
                    chunk.toByteArray()
                }
                byteArrayOf(lengthByte, indexByte) + chunk18Byte
            }
    }

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun fromByteArray(data: ByteArray, bluetoothAddress: String): DKBlueToothWIFIData {
            require(data.size >= 15) { "Invalid data size" }

            val macAddress = data.sliceArray(0 until 6).toUByteArray()
            val channel = (data[6].toUByte() + (data[7].toUByte().toUInt() shl 8)).toUShort()
            val rssi = (data[8].toInt() + (data[9].toInt() shl 8)).toShort()
            val band = (data[10].toUByte() + (data[11].toUByte().toUInt() shl 8)).toUShort()
            val securityType = (data[12].toUByte() + (data[13].toUByte().toUInt() shl 8)).toUShort()
            val ssidLength = (data[14].toUByte() + (data[15].toUByte().toUInt() shl 8)).toUShort()
            val ssid = data.sliceArray(16 until (16 + ssidLength.toInt()))
            val crc16Start = 16 + ssidLength.toInt()
            val crc16 = (data[crc16Start].toUByte() + (data[crc16Start + 1].toUByte().toUInt() shl 8)).toUShort()

            return DKBlueToothWIFIData(macAddress, channel, rssi, band, securityType, ssidLength, ssid = ssid, crc16 = crc16, bluetoothAddress = bluetoothAddress)
        }
    }
}

