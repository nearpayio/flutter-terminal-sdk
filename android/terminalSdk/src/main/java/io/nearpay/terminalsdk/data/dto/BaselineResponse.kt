package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable

data class BaselineResponseData(
    @SerialName("status")
    var status: Boolean
)
