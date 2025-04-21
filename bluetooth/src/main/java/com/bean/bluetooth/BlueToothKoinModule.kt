package com.bean.bluetooth

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val blueToothModule = module {
    single<BlueToothDataSource> { BlueToothDataSourceImpl(androidContext()) }
    single<BlueToothRepository> { BlueToothRepositoryImpl(get()) }
}