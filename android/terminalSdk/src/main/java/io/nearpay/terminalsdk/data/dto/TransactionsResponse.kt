package io.nearpay.terminalsdk.data.dto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionsResponse(
    @SerialName("data")
    val data: List<Transaction>,

    @SerialName("pagination")
    val pagination: Pagination
)
