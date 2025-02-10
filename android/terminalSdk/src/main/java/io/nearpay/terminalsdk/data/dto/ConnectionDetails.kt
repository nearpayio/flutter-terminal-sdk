package io.nearpay.terminalsdk.data.dto

import io.nearpay.softpos.utils.NetworkConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectionInfo(

    @SerialName("user_configuration")
    val userConfiguration: NetworkConfiguration? = null,

    @SerialName("connection_details")
    val connectionDetails: ConnectionDetails? = null,

    @SerialName("battery_details")
    val batteryDetails: BatteryDetails? = null
)

@Serializable
data class ConnectionDetails(
    @SerialName("type")
    val type: String,

    @SerialName("signal")
    val signalStrength: Int? = null,

    @SerialName("up_speed_kbps")
    val linkUpstreamBandwidthKbps: Int,

    @SerialName("down_speed_kbps")
    val linkDownstreamBandwidthKbps: Int,

    @SerialName("raw_data")
    val rawData: String

)

@Serializable
data class BatteryDetails(
    @SerialName("is_charging")
    val isCharging: Boolean,

    @SerialName("battery_percentage")
    val batteryPercentage: Int
)
