package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginSdkData(

    @SerialName("jwt")
    val jwt: String? = null,

    @SerialName("publicKey")
    val publicKey: String
)

