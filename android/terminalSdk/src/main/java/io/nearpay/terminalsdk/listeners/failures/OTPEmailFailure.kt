package io.nearpay.terminalsdk.listeners.failures

sealed class OTPEmailFailure {
    data class AuthenticationFailure(val message: String? = ""): OTPEmailFailure()
}