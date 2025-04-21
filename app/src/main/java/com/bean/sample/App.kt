package com.bean.sample

import android.app.Application
import com.dexatek.bluetooth.dkBlueToothModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dkBlueToothModule)
        }
    }
}