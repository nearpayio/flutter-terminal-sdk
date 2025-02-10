package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshTransactionAuth(

    @SerialName("auth")
    val transactionAuth: Auth,

//    @SerialName("config")
//    val terminalProfile: NullableTerminalProfile?=null
)

