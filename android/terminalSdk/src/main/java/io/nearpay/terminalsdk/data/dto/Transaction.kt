package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Transaction(
    @SerialName("uuid")
    val uuid: String,

    @SerialName("scheme")
    val scheme: String,

    @SerialName("customer_reference_number")
    val customerReferenceNumber: String?,

    @SerialName("pan")
    val pan: String,

    @SerialName("amount_authorized")
    val amountAuthorized: String,

    @SerialName("transaction_type")
    val transactionType: String,

    @SerialName("currency")
    val currency: Currency,

    @SerialName("is_approved")
    val isApproved: Boolean,

    @SerialName("is_reversed")
    val isReversed: Boolean,

    @SerialName("is_reconciled")
    val isReconciled: Boolean,

    @SerialName("start_date")
    val startDate: String,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("performance")
    val performance: List<Performance>
)


@Serializable
data class Currency(
    @SerialName("arabic")
    val arabic: String,

    @SerialName("english")
    val english: String
)

@Serializable
data class Performance(
    @SerialName("type")
    val type: String,

    @SerialName("time_stamp")
    val timeStamp: Long
)


