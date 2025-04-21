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
)