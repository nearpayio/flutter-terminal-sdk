package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import io.nearpay.spin.TransactionType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
@OptIn(ExperimentalSerializationApi::class)
data class TransactionBannerList(

    @SerialName("count")
    val count: Int,

    @SerialName("transactions")
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    val transactionBanners: List<TransactionBanner> = emptyList()

): Parcelable

@Parcelize
@Serializable
data class TransactionBanner(

    @SerialName("uuid")
    val uuid: String,

    @SerialName("scheme")
    val scheme: String,

    @SerialName("pan")
    val pan: String,

    @SerialName("amount_authorized")
    val amount_authorized: String,

    @SerialName("currency")
    val currency: LocalizationField,

    @SerialName("transaction_type")
    val transaction_type: TransactionType,

    @SerialName("is_approved")
    val is_approved: Boolean,

    @SerialName("is_reversed")
    val is_reversed: Boolean,

    @SerialName("start_date")
    val start_date: String,

    @SerialName("start_time")
    val start_time: String,

    @SerialName("retrieval_reference_number")
    val retrieval_reference_number: String? = null,

    @SerialName("customer_reference_number")
    val customer_reference_number: String? = null

): Parcelable