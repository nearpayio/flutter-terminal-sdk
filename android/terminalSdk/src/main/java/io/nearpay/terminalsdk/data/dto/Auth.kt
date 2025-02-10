package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Auth(

    @SerialName("access_token")
    val access_token: String,

    @SerialName("refresh_token")
    val refresh_token: String
)