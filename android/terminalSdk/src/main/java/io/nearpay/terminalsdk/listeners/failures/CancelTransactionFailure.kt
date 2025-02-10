package io.nearpay.terminalsdk.listeners.failures

sealed class CancelTransactionFailure {
    data class GeneralFailure(val message: String) : CancelTransactionFailure()
}
