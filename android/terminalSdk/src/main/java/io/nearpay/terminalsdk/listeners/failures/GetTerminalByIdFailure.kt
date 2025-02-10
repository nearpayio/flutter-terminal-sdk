package io.nearpay.terminalsdk.listeners.failures

sealed class GetTerminalByIdFailure {
    data class GeneralFailure(val message: String? = ""): GetTerminalByIdFailure()
}