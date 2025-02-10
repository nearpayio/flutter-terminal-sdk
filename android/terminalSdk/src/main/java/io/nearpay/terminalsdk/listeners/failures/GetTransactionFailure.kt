package io.nearpay.terminalsdk.listeners.failures

sealed class GetTransactionFailure {
    data class GeneralFailure(val message: String) : GetTransactionFailure()
}
