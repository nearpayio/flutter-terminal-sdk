package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.User
import io.nearpay.terminalsdk.listeners.failures.JWTLoginFailure

interface JWTLoginListener {
    fun onJWTLoginSuccess(user: User)
    fun onJWTLoginFailure(jwtLoginFailure: JWTLoginFailure)
}