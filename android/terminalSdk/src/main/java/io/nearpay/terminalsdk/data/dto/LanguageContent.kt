package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class LanguageContent(
    @SerialName("arabic")
    val arabic: String,

    @SerialName("english")
    val english: String
)
