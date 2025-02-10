package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CancelResponse(
    @SerialName("canceled")
    val canceled: Boolean,
)