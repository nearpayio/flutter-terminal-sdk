package io.nearpay.terminalsdk.listeners.failures

sealed class SendTransactionFailure {
    data class TransactionFailure(val message: String) : SendTransactionFailure()
}
