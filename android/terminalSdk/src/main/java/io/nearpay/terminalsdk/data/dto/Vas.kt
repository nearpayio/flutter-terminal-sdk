package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Vas(
    @SerialName("data")
    val data: String? = null,

    @SerialName("merchant_id")
    val merchant_id: String? = null
)
