package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReconciliationResponse(
    @SerialName("id")
    val id: String,

    @SerialName("date")
    val date: String,

    @SerialName("time")
    val time: String,

    @SerialName("start_date")
    val startDate: String,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("end_date")
    val endDate: String,

    @SerialName("end_time")
    val endTime: String,

    @SerialName("merchant")
    val merchant: Merchant,

    @SerialName("card_acceptor_terminal_id")
    val cardAcceptorTerminalId: String,

    @SerialName("pos_software_version_number")
    val posSoftwareVersionNumber: String,

    @SerialName("card_scheme_sponsor_id")
    val cardSchemeSponsorId: String,

    @SerialName("is_balanced")
    val isBalanced: LabelValue<Boolean>,

    @SerialName("schemes")
    val schemes: List<Scheme>,

    @SerialName("details")
    val details: Details,

    @SerialName("currency")
    val currency: LanguageContent,

    @SerialName("system_trace_audit_number")
    val systemTraceAuditNumber: String
)


@Serializable
data class Details(
    @SerialName("total")
    val total: DetailItem,

    @SerialName("purchase")
    val purchase: DetailItem,

    @SerialName("purchase_reversal")
    val purchaseReversal: DetailItem,

    @SerialName("refund")
    val refund: DetailItem,

    @SerialName("refund_reversal")
    val refundReversal: DetailItem
)

@Serializable
data class DetailItem(
    @SerialName("label")
    val label: LanguageContent,

    @SerialName("total")
    val total: String,

    @SerialName("count")
    val count: Int
)

@Serializable
data class LabelValue<T>(
    @SerialName("label")
    val label: LanguageContent,

    @SerialName("value")
    val value: T
)
