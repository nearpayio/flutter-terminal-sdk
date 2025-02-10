package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginSdkResponse(

    @SerialName("auth")
    val auth: Auth,

    @SerialName("transaction_auth")
    val transactionAuth: Auth
)