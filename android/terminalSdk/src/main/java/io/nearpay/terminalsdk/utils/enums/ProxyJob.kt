package io.nearpay.softpos.core.utils.enums

enum class ProxyJob(val id: Int) {
    START(1),
    CANCEL(2),
    RESULT(3),
    TRANSACTION_STATE(4),
    FORGET(5);

    companion object {
        fun of(id: Int): ProxyJob? {
            return when (id) {
                1 -> START
                2 -> CANCEL
                3 -> RESULT
                4 -> TRANSACTION_STATE
                5 -> FORGET
                else -> null
            }
        }
    }
}