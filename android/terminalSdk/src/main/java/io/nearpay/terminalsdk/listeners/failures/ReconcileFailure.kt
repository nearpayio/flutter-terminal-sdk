package io.nearpay.terminalsdk.listeners.failures

sealed class ReconcileFailure {
    data class GeneralFailure(val message: String) : ReconcileFailure()
}