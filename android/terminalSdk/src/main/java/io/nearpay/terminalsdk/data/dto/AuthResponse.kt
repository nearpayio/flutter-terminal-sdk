package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(

    @SerialName("user")
    val user: LoginUser,

    @SerialName("auth")
    val auth: Auth
)