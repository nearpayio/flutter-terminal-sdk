package io.nearpay.terminalsdk.managers

import android.app.Activity
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import io.nearpay.terminalsdk.data.dto.ConnectionDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import android.provider.Settings
import androidx.core.content.ContextCompat
import io.nearpay.terminalsdk.utils.isAscii
import kotlinx.coroutines.cancelChildren

object ConnectionManager {

    private val _connectivityHasIssue = MutableStateFlow(false)
    val connectivityHasIssue = _connectivityHasIssue.asStateFlow()

    private val _vpnDetected = MutableStateFlow(false)
    val vpnDetected = _vpnDetected.asStateFlow()

    private val parentJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(parentJob + Dispatchers.IO)

    var networkHasTransportWifi = false
    var networkHasTransportCellular = false
    var networkHasTransportEthernet = false

    fun Context.invokeConnectivityManager() {

        val weakContext = WeakReference(this)

        coroutineScope.launch {
            while (isActive) {

                val context = weakContext.get()
                    ?: break

                val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkCapabilities = connectivityManager.activeNetwork
                val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities)

                networkHasTransportWifi = activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
                networkHasTransportCellular = activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true
                networkHasTransportEthernet = activeNetwork?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true

                _connectivityHasIssue.value = when {
                    activeNetwork == null -> true
                    networkHasTransportWifi -> false
                    networkHasTransportCellular -> false
                    networkHasTransportEthernet -> false
                    else -> true
                }



                _vpnDetected.value = when {
                    activeNetwork == null -> false
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
                delay(1000)
            }
        }
    }

    fun Activity.enableWifi() {
        val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
        this.startActivity(intent)
    }

    fun getConnectionDetails(appContext: Context): ConnectionDetails? {

        val connectivityManager = ContextCompat.getSystemService(
            appContext,
            ConnectivityManager::class.java
        ) ?: return null

        val network = connectivityManager.boundNetworkForProcess
            ?: connectivityManager.activeNetwork ?: return null
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return null

        val type = when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "sim"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> "bluetooth"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ethernet"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "vpn"
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> "wifi_aware"
            else -> "unknown"
        }

        val signalStrength =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                networkCapabilities.signalStrength
            else
                null

        return ConnectionDetails(
            type = type,
            signalStrength = signalStrength,
            linkUpstreamBandwidthKbps = networkCapabilities.linkUpstreamBandwidthKbps,
            linkDownstreamBandwidthKbps = networkCapabilities.linkDownstreamBandwidthKbps,
            rawData = if (networkCapabilities.toString()
                    .isAscii()
            ) networkCapabilities.toString() else ""
        )
    }

    fun finish() {
        parentJob.cancelChildren()
    }
}