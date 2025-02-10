package io.nearpay.terminalsdk.data.dto;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable;

@Serializable
data class RefundTransaction(
        val envelope: ReaderEnvelope,
        val location: Location,

        @SerialName("refund_rrn")
        val refundRRN: String
)