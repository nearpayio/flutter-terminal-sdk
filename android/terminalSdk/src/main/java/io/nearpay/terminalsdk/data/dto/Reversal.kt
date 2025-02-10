package io.nearpay.terminalsdk.data.dto

import io.nearpay.terminalsdk.data.dto.Location
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reversal(
    @SerialName("reversal_uuid")
    val reversal_uuid: String,

    @SerialName("location")
    val location: Location?
)
