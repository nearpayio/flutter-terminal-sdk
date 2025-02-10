package io.nearpay.terminalsdk.listeners.failures

sealed class VerifyEmailFailure {
    data class GeneralFailure(val message: String? = ""): VerifyEmailFailure()
}