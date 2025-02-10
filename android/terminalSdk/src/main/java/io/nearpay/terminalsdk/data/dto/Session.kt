//package io.nearpay.terminalsdk.data.dto
//
//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//@Parcelize
//@Serializable
//data class SessionResponse(
//    @SerialName("session")
//    val session: Session
//): Parcelable
//
//@Parcelize
//@Serializable
//data class Session(
//    @SerialName("id")
//    val id: String,
//
//    @SerialName("status")
//    val status: String,
//
//    @SerialName("type")
//    val type: String,
//
//    @SerialName("client_id")
//    val client_id: String,
//
//    @SerialName("amount")
//    val amount: String,
//
//    @SerialName("expired_at")
//    val expired_at: String,
//
//    @SerialName("reference_id")
//    val reference_id: String?,
//
//    @SerialName("created_at")
//    val created_at: String,
//
//    @SerialName("updated_at")
//    val updated_at: String,
//
//    @SerialName("transaction")
//    val transaction: Transaction?,
//) : Parcelable
//
//@Parcelize
//@Serializable
//data class Transaction(
//    @SerialName("id")
//    val id: String?,
//
//    @SerialName("uuid")
//    val uuid: String?,
//
//    @SerialName("amount_authorized")
//    val amount_authorized: String?,
//
//    @SerialName("transaction_currency_code")
//    val transaction_currency_code: String?,
//
//    @SerialName("cardholder_verification_result")
//    val cardholder_verification_result: String?,
//
//    @SerialName("lat")
//    val lat: String?,
//
//    @SerialName("lon")
//    val lon: String?,
//
//    @SerialName("transaction_type")
//    val transaction_type: String?,
//
//    @SerialName("card_scheme_id")
//    val card_scheme_id: String?,
//
//    @SerialName("system_trace_audit_number")
//    val system_trace_audit_number: String?,
//
//    @SerialName("is_approved")
//    val is_approved: Boolean?,
//
//    @SerialName("is_reversed")
//    val is_reversed: Boolean?,
//
//    @SerialName("is_reconcilied")
//    val is_reconcilied: Boolean?,
//
//    @SerialName("device_id")
//    val device_id: String?,
//
//    @SerialName("user_id")
//    val user_id: String?,
//
//    @SerialName("merchant_id")
//    val merchant_id: String?,
//
//    @SerialName("customer_reference_number")
//    val customer_reference_number: String?,
//
//    @SerialName("pos_confirmed")
//    val pos_confirmed: Boolean?,
//
//    @SerialName("created_at")
//    val created_at: String?,
//
//    @SerialName("updated_at")
//    val updated_at: String?,
//
//    @SerialName("receipts")
//    val receipts: List<TransactionReceipt> = emptyList(),
//
//    @SerialName("card_scheme")
//    val card_scheme: LocalizationField?,
//
//    @SerialName("type")
//    val type: LocalizationField?,
//
//    @SerialName("verification_method")
//    val verification_method: LocalizationField?,
//
//    ) : Parcelable
