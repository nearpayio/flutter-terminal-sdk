package io.nearpay.terminalsdk.managers

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat
import io.nearpay.softpos.utils.NetworkConfiguration
import io.nearpay.terminalsdk.utils.CoreAction
import timber.log.Timber

object NetworkConfigurationManager {

    var networkConfiguration: NetworkConfiguration? = null
        private set

    val connectivityManager = ContextCompat.getSystemService(CoreAction.getAppContext(), ConnectivityManager::class.java)

    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder().addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR).build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            Timber.i("onAvailable | user_configuration: $networkConfiguration")

            val bind = when (networkConfiguration) {
                NetworkConfiguration.DEFAULT -> null
                NetworkConfiguration.SIM_PREFERRED -> network
                NetworkConfiguration.SIM_ONLY -> network
                else -> null
            }

            connectivityManager?.bindProcessToNetwork(bind)
        }

        override fun onLost(network: Network) {
            super.onLost(network)

            Timber.i("onLost | user_configuration: $networkConfiguration")

            val shouldFlowSystem = when (networkConfiguration) {
                NetworkConfiguration.DEFAULT -> true
                NetworkConfiguration.SIM_PREFERRED -> true
                NetworkConfiguration.SIM_ONLY -> false
                else -> true
            }

            if (shouldFlowSystem) {
                connectivityManager?.bindProcessToNetwork(null)
            }
        }
    }

    fun setNetworkConfiguration(newNetworkConfiguration: NetworkConfiguration) {

        // Don't do requestNetwork if networkConfiguration has value && it's value not changed
        if (networkConfiguration != null && networkConfiguration?.id == newNetworkConfiguration.id)
            return

        networkConfiguration = newNetworkConfiguration
        Timber.i("setNetworkConfiguration | user_configuration: $networkConfiguration")
        connectivityManager?.requestNetwork(networkRequest, networkCallback)

    }

}
