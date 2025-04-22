package com.bean.datastore

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataStoreModule = module {
    single<DataStoreDataSource> { DataStoreDataSourceImpl(androidContext(), androidContext().applicationInfo.loadLabel(androidContext().packageManager).toString()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
}