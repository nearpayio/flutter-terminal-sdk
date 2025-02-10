package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceInfo(

    @SerialName("device_id")
    val device_id: String,

    @SerialName("model")
    val model: String,

    @SerialName("brand")
    val brand: String,

    @SerialName("type")
    val type: String,

    @SerialName("device_user")
    val deviceUser: String,

    @SerialName("base")
    val base: String,

    @SerialName("incremental")
    val incremental: String,

    @SerialName("sdk")
    val sdk: String,

    @SerialName("board")
    val board: String,

    @SerialName("host")
    val host: String,

    @SerialName("fingerprint")
    val fingerprint: String,

    @SerialName("versioncode")
    val versioncode: String,

    @SerialName("hardware")
    val hardware: String
)