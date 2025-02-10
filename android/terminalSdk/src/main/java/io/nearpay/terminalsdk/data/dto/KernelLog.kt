package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class KernelLog(

    @SerialName("apdu")
    val apdu: List<Apdu>? = null,

    @SerialName("terminal_id")
    val terminal_id: String,

    @SerialName("profile_hash")
    val profile_hash: String,

    @SerialName("state")
    val state: Int? = null,

    @SerialName("transaction_details")
    val transactionDetails: TransactionDetails? = null,

    @SerialName("transaction_id")
    val transaction_id: String? = null,
): Parcelable

@Parcelize
@Serializable
data class Apdu(
    @SerialName("response") // from card
    var response: String? = null,

    @SerialName("command") // from kernel
    var command: String? = null,
): Parcelable

@Parcelize
@Serializable
data class TransactionDetails(
    @SerialName("amount")
    val amount: Long,

    @SerialName("amountOther")
    val amountOther: Long,

    @SerialName("currency")
    val currency: Int,

    @SerialName("currencyExponent")
    val currencyExponent: Int,

    @SerialName("transactionType")
    val transactionType: Int,

    @SerialName("transactionTimestamp")
    val transactionTimestamp: Long,

    @SerialName("transactionTimestampOffsetSeconds")
    val transactionTimestampOffsetSeconds: Int,

    @SerialName("upn_4bytes")
    val upn_4bytes: String,

    @SerialName("isRestarted")
    val isRestarted: Boolean,

    @SerialName("tlvEncoded")
    val tlvEncoded: String,
): Parcelable