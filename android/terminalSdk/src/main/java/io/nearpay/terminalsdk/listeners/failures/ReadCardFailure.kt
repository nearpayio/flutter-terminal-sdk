package io.nearpay.terminalsdk.listeners.failures

sealed class ReadCardFailure {
    data class GeneralFailure(val message: String? = ""): ReadCardFailure()
}