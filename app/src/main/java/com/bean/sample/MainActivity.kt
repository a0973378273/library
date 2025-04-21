package com.bean.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.bean.sample.ui.theme.SampleTheme
import com.dexatek.bluetooth.data.DKBlueToothWIFIData
import com.dexatek.bluetooth.data.DKWiredNetworkData
import org.koin.android.ext.android.inject
import usecase.StartDiscoveryUseCase
import usecase.StopDiscoveryUseCase
import usecase.ConnectUseCase
import usecase.DisconnectUseCase
import usecase.ReadMACAddressUseCase
import usecase.ReadWifiListUseCase
import usecase.ReadWiredNetworkUseCase
import usecase.SetDynamicIpInWiredNetworkUseCase
import usecase.SetOTPTokenUseCase
import usecase.SetStaticIpInWiredNetworkUseCase
import usecase.SetWifiUseCase


class MainActivity : ComponentActivity() {
    private val startDiscoveryUseCase: StartDiscoveryUseCase by inject()
    private val stopDiscoveryUseCase: StopDiscoveryUseCase by inject()
    private val connectUseCase: ConnectUseCase by inject()
    private val disconnectUseCase: DisconnectUseCase by inject()
    private val readWifiListUseCase: ReadWifiListUseCase by inject()
    private val setWifiUseCase: SetWifiUseCase by inject()
    private val setOTPTokenUseCase: SetOTPTokenUseCase by inject()
    private val readMACAddressUseCase: ReadMACAddressUseCase by inject()
    private val readWiredNetworkUseCase: ReadWiredNetworkUseCase by inject()
    private val setStaticIpInWiredNetworkUseCase: SetStaticIpInWiredNetworkUseCase by inject()
    private val setDynamicIpInWiredNetworkUseCase: SetDynamicIpInWiredNetworkUseCase by inject()

    private val blueToothAddress = "00:00:00:00:00:00"
    private val deviceName = "00:00:00:00:00:00"
    private val otpToken = "00:00:00:00:00:00"
    private val password = "00:00:00:00:00:00"
    private val ipAddress = ""
    private val maskAddress = ""
    private val routerAddress = ""


    private var dkBlueToothWIFIData : DKBlueToothWIFIData? = null
    private var macAddress : String? = null
    private var dkWiredNetworkData : DKWiredNetworkData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { it }
            }
        }

//        startDiscoveryUseCase(
//            onSuccess = { println(it.name) },
//            onError = { println(it.message) }
//        )
//
//        stopDiscoveryUseCase()
//
//        connectUseCase(
//            bluetoothAddress =  blueToothAddress,
//            onSuccess = { println("連線成功") },
//            onError = { println(it.message) }
//        )
//
//        disconnectUseCase(bluetoothAddress = blueToothAddress)
//
//        readWifiListUseCase(
//            bluetoothAddress =  blueToothAddress,
//            onSuccess = { dkBlueToothWIFIData = it },
//            onError = { println(it.message) }
//        )
//
//        setWifiUseCase(
//            dkBlueToothWIFIData = dkBlueToothWIFIData!!,
//            password = password,
//            onSuccess = { println("設置成功") },
//            onError = { println(it.message) }
//        )
//
//        setOTPTokenUseCase(
//            blueToothAddress = blueToothAddress,
//            deviceName = deviceName,
//            otpToken = otpToken,
//            onSuccess = { println("設置成功") },
//            onError = { println(it.message) }
//        )
//
//        readMACAddressUseCase(
//            bluetoothAddress = blueToothAddress,
//            onSuccess = { macAddress = it },
//            onError = { println(it.message) }
//        )

        readWiredNetworkUseCase(
            bluetoothAddress = blueToothAddress,
            onSuccess = { dkWiredNetworkData = it },
            onError = { println(it.message) }
        )

        setStaticIpInWiredNetworkUseCase(
            blueToothAddress = blueToothAddress,
            ipAddress = ipAddress,
            maskAddress = maskAddress,
            routerAddress = routerAddress,
            onSuccess = { println("設置成功") },
            onError = { println(it.message) }
        )

        setDynamicIpInWiredNetworkUseCase(
            blueToothAddress = blueToothAddress,
            onSuccess = { println("設置成功") },
            onError = { println(it.message) }
        )
    }
}
