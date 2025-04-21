package com.dexatek.bluetooth

import org.koin.core.module.Module
import org.koin.dsl.module
import usecase.ConnectUseCase
import usecase.DisconnectUseCase
import usecase.StartDiscoveryUseCase
import usecase.StopDiscoveryUseCase

@JvmField
val dkBlueToothModule = module {
    includes(com.bean.bluetooth.blueToothModule)
    single<DKBlueToothRepository> { DKBlueToothRepositoryImpl(get()) }
    factory { StopDiscoveryUseCase(get()) }
    factory { StartDiscoveryUseCase(get()) }
    factory { ConnectUseCase(get()) }
    factory { DisconnectUseCase(get()) }
    // TODO add useCase
}

object DKBlueToothModuleProvider {
    @JvmStatic
    fun getModule(): Module = dkBlueToothModule
}