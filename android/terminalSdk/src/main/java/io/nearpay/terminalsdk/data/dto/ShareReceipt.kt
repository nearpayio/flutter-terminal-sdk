package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShareReceipt(

    @SerialName("mobile")
    val mobile: String? = null,

    @SerialName("email")
    val email: String? = null,

    @SerialName("transaction_uuid")
    val transaction_uuid: String
)