package io.nearpay.terminalsdk.listeners.failures

sealed class RefreshPosTokenFailure {
    data class GeneralFailure(val message: String) : RefreshPosTokenFailure()
}