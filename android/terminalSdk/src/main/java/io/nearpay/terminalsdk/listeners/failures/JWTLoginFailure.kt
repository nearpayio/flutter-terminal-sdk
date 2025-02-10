package io.nearpay.terminalsdk.listeners.failures

sealed class JWTLoginFailure {
    data class LoginFailure(val message: String) : JWTLoginFailure()
}