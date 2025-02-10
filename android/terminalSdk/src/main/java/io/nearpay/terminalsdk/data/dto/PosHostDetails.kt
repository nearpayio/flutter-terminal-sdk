package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class PosHostDetails(
    @SerialName("credit")
    val credit: DetailItem,

    @SerialName("debit")
    val debit: DetailItem,

    @SerialName("total")
    val total: DetailItem
)