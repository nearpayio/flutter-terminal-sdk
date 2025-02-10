package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PerformanceItem(

    @SerialName("type")
    val type: String,

    @SerialName("time_stamp")
    val time_stamp: Long
)