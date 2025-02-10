package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReceiptsResponse(
    @SerialName("reciepts")
    val receipts: List<Receipt>
)

@Serializable
data class Receipt(
    @SerialName("id")
    val id: String,

    @SerialName("merchant")
    val merchant: Merchant,

    @SerialName("type")
    val type: String,

    @SerialName("start_date")
    val startDate: String,

    @SerialName("start_time")
    val startTime: String,

    @SerialName("card_scheme_sponsor")
    val cardSchemeSponsor: String,

    @SerialName("tid")
    val tid: String,

    @SerialName("system_trace_audit_number")
    val systemTraceAuditNumber: String,

    @SerialName("pos_software_version_number")
    val posSoftwareVersionNumber: String,

    @SerialName("retrieval_reference_number")
    val retrievalReferenceNumber: String,

    @SerialName("card_scheme")
    val cardScheme: CardScheme,

    @SerialName("transaction_type")
    val transactionType: TransactionType,

    @SerialName("pan")
    val pan: String,

    @SerialName("card_expiration")
    val cardExpiration: String,

    @SerialName("amount_authorized")
    val amountAuthorized: Amount,

    @SerialName("amount_other")
    val amountOther: Amount,

    @SerialName("currency")
    val currency: LanguageContent,

    @SerialName("status_message")
    val statusMessage: LanguageContent,

    @SerialName("is_approved")
    val isApproved: Boolean,

    @SerialName("is_refunded")
    val isRefunded: Boolean,

    @SerialName("is_reversed")
    val isReversed: Boolean,

    @SerialName("approval_code")
    val approvalCode: ApprovalCode,

    @SerialName("verification_method")
    val verificationMethod: LanguageContent,

    @SerialName("end_date")
    val endDate: String,

    @SerialName("end_time")
    val endTime: String,

    @SerialName("receipt_line_one")
    val receiptLineOne: LanguageContent,

    @SerialName("receipt_line_two")
    val receiptLineTwo: LanguageContent,

    @SerialName("thanks_message")
    val thanksMessage: LanguageContent,

    @SerialName("save_receipt_message")
    val saveReceiptMessage: LanguageContent,

    @SerialName("entry_mode")
    val entryMode: String,

    @SerialName("action_code")
    val actionCode: String,

    @SerialName("application_identifier")
    val applicationIdentifier: String,

    @SerialName("terminal_verification_result")
    val terminalVerificationResult: String,

    @SerialName("transaction_state_information")
    val transactionStateInformation: String,

    @SerialName("cardholader_verfication_result")
    val cardholderVerificationResult: String,

    @SerialName("cryptogram_information_data")
    val cryptogramInformationData: String,

    @SerialName("application_cryptogram")
    val applicationCryptogram: String,

    @SerialName("kernel_id")
    val kernelId: String,

    @SerialName("payment_account_reference")
    val paymentAccountReference: String,

    @SerialName("pan_suffix")
    val panSuffix: String,

    @SerialName("transaction_uuid")
    val transactionUuid: String,

    @SerialName("qr_code")
    val qrCode: String
)

@Serializable
data class CardScheme(
    @SerialName("name")
    val name: LanguageContent,

    @SerialName("id")
    val id: String
)

@Serializable
data class TransactionType(
    @SerialName("name")
    val name: LanguageContent,

    @SerialName("id")
    val id: String
)

@Serializable
data class Amount(
    @SerialName("label")
    val label: LanguageContent,

    @SerialName("value")
    val value: String
)

@Serializable
data class ApprovalCode(
    @SerialName("value")
    val value: String,

    @SerialName("label")
    val label: LanguageContent
)
