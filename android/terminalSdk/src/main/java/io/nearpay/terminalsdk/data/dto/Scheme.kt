package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Scheme(
    @SerialName("name")
    val name: LabelValue<String>,

    @SerialName("pos")
    val pos: PosHostDetails,

    @SerialName("host")
    val host: PosHostDetails,

    @SerialName("is_balanced")
    val isBalanced: Boolean
)