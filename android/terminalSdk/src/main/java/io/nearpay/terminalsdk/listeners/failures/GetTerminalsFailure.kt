package io.nearpay.terminalsdk.listeners.failures

sealed class GetTerminalsFailure {
    data class FetchingFailure(val message: String? = ""): GetTerminalsFailure()
}