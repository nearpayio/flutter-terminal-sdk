package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionLookupResponse(
    @SerialName("uuid")
    val transaction_uuid: String? = null
)