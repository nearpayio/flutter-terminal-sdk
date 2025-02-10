package io.nearpay.terminalsdk.listeners.failures

sealed class GetReconciliationListFailure {
    data class GeneralFailure(val message: String? = ""): GetReconciliationListFailure()
}