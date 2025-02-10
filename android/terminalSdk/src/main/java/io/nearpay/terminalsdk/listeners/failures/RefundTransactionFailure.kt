package io.nearpay.terminalsdk.listeners.failures

sealed class RefundTransactionFailure {
    data class GeneralFailure(val message: String) : RefundTransactionFailure()
}
