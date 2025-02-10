package io.nearpay.terminalsdk.listeners.failures

sealed class GetTransactionsListFailure {
    data class GeneralFailure(val message: String) : GetTransactionsListFailure()
}
