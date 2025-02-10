package io.nearpay.softpos.core.utils.enums

import io.nearpay.terminalsdk.R

enum class TransactionStatus(val id: String, val stringRes: Int, val colorRes: Int) {
    UNDEFINED("", R.string.undefined, R.color.dark_gray),
    CREATED("created", R.string.created, R.color.dark_gray),
    APPROVED("approved", R.string.approved, R.color.success_green),
    REJECTED("rejected", R.string.rejected, R.color.error_red),
    REVERSED("reversed", R.string.reversed, R.color.reversal);

    companion object {
        fun getStatus(value: String) = values().firstOrNull {
            it.id == value
        } ?: UNDEFINED
    }
}