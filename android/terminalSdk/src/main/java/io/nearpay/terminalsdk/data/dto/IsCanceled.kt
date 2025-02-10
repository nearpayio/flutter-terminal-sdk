package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IsCanceled (
    @SerialName("canceled")
    val canceled: Boolean
)