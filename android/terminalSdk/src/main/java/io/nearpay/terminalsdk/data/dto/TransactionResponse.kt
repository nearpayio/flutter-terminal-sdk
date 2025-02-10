package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.JsonElement

@Serializable
data class TransactionResponse(
    @SerialName("id") val id: String,
    @SerialName("action_code") val actionCode: String,
    @SerialName("type") val type: String,
    @SerialName("status") val status: Status,
    @SerialName("amount") val amount: String,
    @SerialName("currency") val currency: String,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("completed_at") val completedAt: String? = null,
    @SerialName("canceled_at") val canceledAt: String? = null,
    @SerialName("cancel_reason") val cancelReason: String? = null,
    @SerialName("reference_id") val referenceId: String? = null,
    @SerialName("order_id") val orderId: String? = null,
    @SerialName("approval_code") val approvalCode: String? = null,
    @SerialName("pin_required") val pinRequired: Boolean? = null,
    @SerialName("terminal") val terminal: Terminal? = null,
    @SerialName("attempts") val attempts: Int? = null,
    @SerialName("user") val user: Map<String, JsonElement>? = null,
    @SerialName("device") val device: Map<String, JsonElement>? = null,
    @SerialName("app") val app: Map<String, JsonElement>? = null,
    @SerialName("card") val card: Card,
    @SerialName("events") val events: List<Event>
) {
    @Serializable
    enum class Status {
        @SerialName("approved") APPROVED,
        @SerialName("declined") DECLINED
    }

    @Serializable
    data class Terminal(
        @SerialName("terminal_id") val terminalId: String,
        @SerialName("bank_id") val bankId: String,
        @SerialName("merchant_id") val merchantId: String,
        @SerialName("vendor_id") val vendorId: String,
        @SerialName("merchant_catgory_code") val merchantCategoryCode: String
    )

    @Serializable
    data class Card(
        @SerialName("pan") val pan: String,
        @SerialName("exp") val exp: String
    )

    @Serializable
    data class Event(
        @SerialName("rrn") val rrn: String,
        @SerialName("stan") val stan: String,
        @SerialName("type") val type: String,
        @SerialName("status") val status: Status,
        @SerialName("receipt") val receipt: Receipt
    ) {
        @Serializable
        data class Receipt(
            @SerialName("type") val type: ReceiptType,
            @SerialName("data") val data: JsonElement // Placeholder for mada receipt data structure
        )

        @Serializable
        enum class ReceiptType {
            @SerialName("mada") MADA,
            @SerialName("meps") MEPS
        }
    }
}
