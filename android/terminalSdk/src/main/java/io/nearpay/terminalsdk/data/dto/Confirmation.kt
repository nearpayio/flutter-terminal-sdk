package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Confirmation(

    @SerialName("transaction_uuid")
    val transaction_uuid: String,

    @SerialName("is_received")
    val received: Boolean,

    @SerialName("is_decrypted")
    val decrypted: Boolean,

    @SerialName("is_parsed")
    val parsed: Boolean,

    @SerialName("performance")
    var performance: List<PerformanceItem> = emptyList()
)
