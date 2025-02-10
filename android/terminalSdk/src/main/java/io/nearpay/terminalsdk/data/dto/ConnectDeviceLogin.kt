package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectDeviceLogin(

    @SerialName("email")
    val email: String,

    @SerialName("device")
    val model: DeviceInfo,

    @SerialName("app")
    val app: String,

    @SerialName("publicKey")
    val publicKey: String

)