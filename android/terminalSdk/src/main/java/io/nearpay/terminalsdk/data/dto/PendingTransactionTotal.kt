package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import io.nearpay.terminalsdk.data.dto.LocalizationField
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class PendingTransactionTotal(

    @SerialName("total")
    val total: String,

    @SerialName("currency")
    val currency: LocalizationField

): Parcelable