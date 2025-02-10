package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("pagination")
data class Pagination(
    @SerialName("total_pages")
    val total_pages: Int,

    @SerialName("current_page")
    val current_page: Int,

    @SerialName("total_data")
    val total_data: Int
)