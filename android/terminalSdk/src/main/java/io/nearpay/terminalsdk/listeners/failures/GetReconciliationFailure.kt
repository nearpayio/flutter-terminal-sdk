package io.nearpay.terminalsdk.listeners.failures

sealed class GetReconciliationFailure {
    data class GeneralFailure(val message: String? = ""): GetReconciliationFailure()
}