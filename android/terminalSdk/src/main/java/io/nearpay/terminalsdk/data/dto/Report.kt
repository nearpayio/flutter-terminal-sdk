package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReportList(

    @SerialName("total")
    val total: Int,

    @SerialName("months")
    val months: List<ReportMonth>
)

@Parcelize
@Serializable
data class ReportMonth(

    @SerialName("currency")
    val currency: LocalizationField,

    @SerialName("month")
    val month: String,

    @SerialName("days")
    val days: List<ReportDay>
): Parcelable

@Parcelize
@Serializable
data class ReportDay(

    @SerialName("date")
    val date: String,

    @SerialName("total")
    var total: String
): Parcelable

