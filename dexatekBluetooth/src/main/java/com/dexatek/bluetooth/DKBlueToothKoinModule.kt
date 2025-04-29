package com.dexatek.bluetooth

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module
import usecase.ConnectUseCase
import usecase.DisconnectUseCase
import usecase.ReadMACAddressUseCase
import usecase.ReadWifiListUseCase
import usecase.ReadWiredNetworkUseCase
import usecase.SetDynamicIpInWiredNetworkUseCase
import usecase.SetOTPTokenUseCase
import usecase.SetStaticIpInWiredNetworkUseCase
import usecase.SetWifiUseCase
import usecase.StartDiscoveryUseCase
import usecase.StopDiscoveryUseCase

@JvmField
val dkBlueToothModule = module {
    single<BlueToothDataSource> { BlueToothDataSourceImpl(androidContext()) }
    single<DKBlueToothRepository> { DKBlueToothRepositoryImpl(get()) }
    factory { StopDiscoveryUseCase(get()) }
    factory { StartDiscoveryUseCase(get()) }
    factory { ConnectUseCase(get()) }
    factory { DisconnectUseCase(get()) }
    factory { ReadMACAddressUseCase(get()) }
    factory { ReadWifiListUseCase(get()) }
    factory { ReadWiredNetworkUseCase(get()) }
    factory { SetDynamicIpInWiredNetworkUseCase(get()) }
    factory { SetOTPTokenUseCase(get()) }
    factory { SetStaticIpInWiredNetworkUseCase(get()) }
    factory { SetWifiUseCase(get()) }
}

object DKBlueToothModuleProvider {
    @JvmStatic
    fun getModule(): Module = dkBlueToothModule
}