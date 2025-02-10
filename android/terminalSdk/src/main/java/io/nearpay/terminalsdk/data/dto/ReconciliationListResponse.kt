package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReconciliationListResponse(
    @SerialName("data")
    val data: List<ReconciliationItem>,

    @SerialName("pagination")
    val pagination: Pagination
)

@Serializable
data class ReconciliationItem(
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

    @SerialName("is_balanced")
    val isBalanced: LabelValue<Boolean>,

    @SerialName("total")
    val total: String,

    @SerialName("currency")
    val currency: LanguageContent
)
