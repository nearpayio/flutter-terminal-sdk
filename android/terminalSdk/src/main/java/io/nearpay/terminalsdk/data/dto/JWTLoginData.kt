package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class JWTLoginData (
    @SerialName("jwt")
    val jwt: String,

    @SerialName("device_token")
    val deviceToken: String
)