//package io.nearpay.terminalsdk.data.dto
//
//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//@Parcelize
//@Serializable
//data class Reconciliation(
//
//    @SerialName("id")
//    val id: String,
//
//    @SerialName("date")
//    val date: String,
//
//    @SerialName("time")
//    val time: String,
//
//    @SerialName("is_balanced")
//    val is_balanced: LabelField<Boolean>,
//
//    @SerialName("details")
//    val details: ReconciliationDetails,
//
//    @SerialName("schemes")
//    val schemes: List<ReconciliationSchemes> = emptyList(),
//
//    @SerialName("currency")
//    val currency: LocalizationField,
//
//    @SerialName("qr_code")
//    val qr_code: String,
//
//    @SerialName("merchant")
//    val merchant: Merchant,
//
//    @SerialName("card_acceptor_terminal_id")
//    val tid: String,
//
//    @SerialName("system_trace_audit_number")
//    val system_trace_audit_number: String,
//
//    @SerialName("pos_software_version_number")
//    val pos_software_version_number: String
//
//
//): Parcelable
//
//@Parcelize
//@Serializable
//data class ReconciliationDetails(
//
//    @SerialName("purchase")
//    val purchase: ReconciliationLabelField,
//
//    @SerialName("refund")
//    val refund: ReconciliationLabelField,
//
//    @SerialName("purchase_reversal")
//    val purchase_reversal: ReconciliationLabelField,
//
//    @SerialName("refund_reversal")
//    val refund_reversal: ReconciliationLabelField,
//
//    @SerialName("total")
//    val total: ReconciliationLabelField
//
//) : Parcelable
//
//@Parcelize
//@Serializable
//data class ReconciliationSchemes(
//
//    @SerialName("name")
//    val name: LabelField<String>,
//
//    @SerialName("pos")
//    val pos: ReconciliationSchemesDetails,
//
//    @SerialName("host")
//    val host: ReconciliationSchemesDetails
//
//) : Parcelable
//
//@Parcelize
//@Serializable
//data class ReconciliationSchemesDetails(
//
//    @SerialName("debit")
//    val debit: ReconciliationLabelField,
//
//    @SerialName("credit")
//    val credit: ReconciliationLabelField,
//
//    @SerialName("total")
//    val total: ReconciliationLabelField,
//
//    ) : Parcelable
//
//
//@Parcelize
//@Serializable
//data class ReconciliationLabelField(
//    @SerialName("label")
//    val label: LocalizationField,
//
//    @SerialName("total")
//    val total: String,
//
//    @SerialName("count")
//    val count: Int
//) : Parcelable