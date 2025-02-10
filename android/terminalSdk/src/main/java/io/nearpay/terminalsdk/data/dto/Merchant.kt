package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Merchant(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: LanguageContent,

    @SerialName("address")
    val address: LanguageContent,

    @SerialName("category_code")
    val categoryCode: String
)