package io.nearpay.terminalsdk.listeners.failures

sealed class ConnectTerminalFailure {
    data class Failure(val message: String? = ""): ConnectTerminalFailure()
}