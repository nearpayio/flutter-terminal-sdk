package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.User
import io.nearpay.terminalsdk.data.dto.AuthResponse
import io.nearpay.terminalsdk.listeners.failures.VerifyEmailFailure

interface VerifyEmailListner {
    fun onVerifyEmailSuccess(user: User)
    fun onVerifyEmailFailure(verifyEmailFailure: VerifyEmailFailure)
}