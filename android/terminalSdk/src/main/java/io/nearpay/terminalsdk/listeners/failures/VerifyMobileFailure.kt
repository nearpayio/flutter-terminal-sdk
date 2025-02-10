package io.nearpay.terminalsdk.listeners.failures

sealed class VerifyMobileFailure {
    data class VerificationFailure(val message: String? = ""): VerifyMobileFailure()
}