package com.bean.bluetooth

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

/**
 * Request bluetooth permission
 *
 * @param action callback when permission granted
 *
 * Note: Add the following permissions to your AndroidManifest.xml file
 *     <uses-permission android:name="android.permission.BLUETOOTH" />
 *     <uses-permission android:name="android.permission.BLUETOOTH_SCAN"/>
 *     <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
 *     <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
 *     <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 *     <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 *
 *  Example:
 *     var requestBlueToothPermission by remember { mutableStateOf(false) }
 *     RequestBlueToothPermission { isGrant ->
 *         if (isGrant) {
 *             requestBlueToothPermission = true
 *         }
 *     }
 */
@Composable
fun RequestBlueToothPermission(action: (Boolean) -> Unit) {
    val bluetoothPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { isGrant ->
        action(isGrant.values.all { it })
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            action(true)
        }
    }
}