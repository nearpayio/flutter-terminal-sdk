package io.nearpay.terminalsdk.listeners.failures

sealed class OTPMobileFailure {
    data class AuthenticationFailure(val message: String? = ""): OTPMobileFailure()
}