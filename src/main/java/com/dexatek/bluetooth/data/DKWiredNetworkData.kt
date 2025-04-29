package com.dexatek.bluetooth.data

data class DKWiredNetworkData(

    /**
     * The device's IP address is dynamic (DHCP) or static
     * true: dynamic (DHCP)
     * false: static
     */
    val isDynamicIp: Boolean = false,
    val isCableDetection: Boolean = false,
    val deviceIp: String = "0.0.0.0",
    val routerIp: String = "0.0.0.0",
    val maskIp: String = "0.0.0.0"

){
    companion object {
        fun from(data: ByteArray): DKWiredNetworkData {
            println("DKWiredNetworkData: ${data.joinToString(separator = ":") { "%02X".format(it) }}")
            val isDynamicIp = ((data[0].toInt() shr 1) and 0x01) == 1
            val isCableDetection = ((data[0].toInt() shr 2) and 0x01) == 1
            val deviceIp = data.slice(1..4).joinToString(".") { it.toUByte().toString() }
            val gatewayIp = data.slice(5..8).joinToString(".") { it.toUByte().toString() }
            val subnetMask = data.slice(9..12).joinToString(".") { it.toUByte().toString() }
            println("isDynamicIp: $isDynamicIp")
            println("isCableDetection: $isCableDetection")
            println("deviceIp: $deviceIp")
            println("gatewayIp: $gatewayIp")
            println("subnetMask: $subnetMask")
            return DKWiredNetworkData(
                isDynamicIp = isDynamicIp,
                isCableDetection = isCableDetection,
                deviceIp = deviceIp,
                routerIp = gatewayIp,
                maskIp = subnetMask
            )
        }
    }
}