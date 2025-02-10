package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppUpdate(

    @SerialName("forcedUpgrade")
    val forcedUpgrade: Boolean = false,

    @SerialName("recommendUpgrade")
    val recommendUpgrade: Boolean = false
)