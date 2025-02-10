package io.nearpay.softpos.core.utils.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TransactionType(val id: Int) {
    @SerialName("-1")
    UNDEFINED(-1),

    @SerialName("00")
    PURCHASE(0X00),

    @SerialName("01")
    CASH(0X01),

    @SerialName("17")
    CASH_DISBURSEMENT(0X17),

    @SerialName("09")
    PURCHASE_WITH_CASHBACK(0X09),

    @SerialName("20")
    REFUND(0X20);

    companion object {
        fun getType(id: String) = id.toIntOrNull()?.let { getType(it) } ?: UNDEFINED

        fun getType(id: Int) = values().firstOrNull {
            it.id == id
        } ?: UNDEFINED
    }
}