package com.bean.sample;

import android.app.Activity;
import android.os.Bundle;

import com.bean.smaple.R;
import usecase.ConnectUseCase;
import usecase.DisconnectUseCase;
import usecase.ReadMACAddressUseCase;
import usecase.ReadWifiListUseCase;
import usecase.ReadWiredNetworkUseCase;
import usecase.SetDynamicIpInWiredNetworkUseCase;
import usecase.SetOTPTokenUseCase;
import usecase.SetStaticIpInWiredNetworkUseCase;
import usecase.SetWifiUseCase;
import usecase.StartDiscoveryUseCase;
import usecase.StopDiscoveryUseCase;

import static org.koin.java.KoinJavaComponent.get;

import com.dexatek.bluetooth.data.DKBlueToothWIFIData;
import com.dexatek.bluetooth.data.DKWiredNetworkData;

//import com.dexatek.bluetooth.usecase.ConnectUseCase;
//import com.dexatek.bluetooth.usecase.StartDiscoveryUseCase;
//import com.dexatek.bluetooth.usecase.StopDiscoveryUseCase;

public class MainActivityJava extends Activity {
    private final String blueToothAddress = "AA:BB:CC:11:22:33"; // 藍牙裝置位址
    private final String password = "AA:BB:CC:11:22:33"; // 藍牙裝置位址
    private final String deviceName = "AA:BB:CC:11:22:33"; // 藍牙裝置位址
    private final String otpToken = "AA:BB:CC:11:22:33"; // 藍牙裝置位址

    private final String ipAddress = "192.168.1.100";
    private final String maskAddress = "255.255.255.0";
    private final String routerAddress = "192.168.1.1";
    private DKBlueToothWIFIData dkBlueToothWIFIData = null;
    private DKWiredNetworkData dkWiredNetworkData = null;
    private String macAddress = null;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main_layout);

//            StartDiscoveryUseCase startDiscoveryUseCase = get(StartDiscoveryUseCase.class);
//            startDiscoveryUseCase.execute(
//                    data -> System.out.println("成功: " + data.getName()),
//                    exception -> System.err.println("錯誤: " + exception.getMessage())
//            );
//
//            StopDiscoveryUseCase stopDiscoveryUseCase = get(StopDiscoveryUseCase.class);
//            stopDiscoveryUseCase.execute();
//
//            ConnectUseCase connectUseCase = get(ConnectUseCase.class);
//            connectUseCase.execute(
//                    blueToothAddress,
//                    () -> System.out.println("連線成功！"),
//                    exception -> System.err.println("連線失敗: " + exception.getMessage())
//            );
//
//            DisconnectUseCase disconnectUseCase = get(DisconnectUseCase.class);
//            disconnectUseCase.execute(blueToothAddress);
//
//            ReadWifiListUseCase readWifiListUseCase = get(ReadWifiListUseCase.class);
//            readWifiListUseCase.execute(
//                    blueToothAddress,
//                    data -> dkBlueToothWIFIData = data,
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );
//
//            SetWifiUseCase setWifiUseCase = get(SetWifiUseCase.class);
//            setWifiUseCase.execute(
//                    dkBlueToothWIFIData,
//                    password,
//                    () -> System.out.println("設定成功！"),
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );
//
//            SetOTPTokenUseCase setOTPTokenUseCase = get(SetOTPTokenUseCase.class);
//            setOTPTokenUseCase.execute(
//                    blueToothAddress,
//                    deviceName,
//                    otpToken,
//                    () -> System.out.println("設定成功！"),
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );
//
//            ReadMACAddressUseCase readMACAddressUseCase = get(ReadMACAddressUseCase.class);
//            readMACAddressUseCase.execute(
//                    blueToothAddress,
//                    data -> macAddress = data,
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );
//
//            ReadWiredNetworkUseCase readWiredNetworkUseCase = get(ReadWiredNetworkUseCase.class);
//            readWiredNetworkUseCase.execute(
//                    blueToothAddress,
//                    data -> dkWiredNetworkData = data,
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );
//
//            SetStaticIpInWiredNetworkUseCase setStaticIpInWiredNetworkUseCase = get(SetStaticIpInWiredNetworkUseCase.class);
//            setStaticIpInWiredNetworkUseCase.execute(
//                    blueToothAddress,
//                    ipAddress,
//                    maskAddress,
//                    routerAddress,
//                    () -> System.out.println("設定成功！"),
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );
//
//            SetDynamicIpInWiredNetworkUseCase setDynamicIpInWiredNetworkUseCase = get(SetDynamicIpInWiredNetworkUseCase.class);
//            setDynamicIpInWiredNetworkUseCase.execute(
//                    blueToothAddress,
//                    () -> System.out.println("設定成功！"),
//                    exception -> System.err.println("讀取失敗: " + exception.getMessage())
//            );

//            DisconnectUseCase disconnectUseCase = get(DisconnectUseCase.class);
//            disconnectUseCase.invoke(
//                    "AA:BB:CC:11:22:33",
//                    () -> System.out.println("成功"),
//                    e -> System.err.println("失敗: " + e.getMessage())
//            );
        }
}
