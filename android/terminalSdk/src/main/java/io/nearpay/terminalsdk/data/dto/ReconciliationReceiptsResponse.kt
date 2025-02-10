package io.nearpay.terminalsdk.data.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReconciliationReceiptsResponse(
    @SerialName("receipt")
    val receipt: ReconciliationReceipt
)

@Serializable
data class ReconciliationReceipt(
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

    @SerialName("details")
    val details: Details,

    @SerialName("schemes")
    val schemes: List<Scheme>,

    @SerialName("currency")
    val currency: LanguageContent,

    @SerialName("system_trace_audit_number")
    val systemTraceAuditNumber: String,

    @SerialName("merchant_id")
    val merchantId: String,

    @SerialName("device_id")
    val deviceId: String,

    @SerialName("client_id")
    val clientId: String,

    @SerialName("terminal")
    val terminal: Terminal,

    @SerialName("reconciliation")
    val reconciliation: Reconciliation,

    @SerialName("user_id")
    val userId: String?,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("qr_code")
    val qrCode: String
)

@Serializable
data class Terminal(
    @SerialName("id")
    val id: String
)

@Serializable
data class Reconciliation(
    @SerialName("id")
    val id: String
)
