package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginUser(
    @SerialName("name")
    val name: String,

    @SerialName("email")
    val email: String,

    @SerialName("mobile")
    val mobile: String,

    @SerialName("userUUID")
    var userUUID: String? = null // default value to pass the required field error
    )