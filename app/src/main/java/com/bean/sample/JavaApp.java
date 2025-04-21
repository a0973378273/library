package com.bean.sample;

import static org.koin.android.ext.koin.KoinExtKt.androidContext;
import static org.koin.core.context.DefaultContextExtKt.startKoin;

import android.app.Application;

import com.dexatek.bluetooth.DKBlueToothModuleProvider;


public class JavaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startKoin(koinApp -> {
            androidContext(koinApp, this);
            koinApp.modules(DKBlueToothModuleProvider.getModule());
            return null;
        });
    }
}
