package io.nearpay.terminalsdk

import android.content.Intent
import android.content.IntentSender

interface BluetoothObserver {
    fun onRequestBluetoothPermissions()
    fun onBluetoothPermissionsDenied()
    fun onRequestEnableBluetooth(enableBluetooth: Intent)
    fun onDeviceFound(chooserLauncher: IntentSender)
    fun onDeviceNotFound()
    fun onFindingDevice()
    fun onDeviceConnected(deviceName: String)
    fun onDeviceDisconnected()
}