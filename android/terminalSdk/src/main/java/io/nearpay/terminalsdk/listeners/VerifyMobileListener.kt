package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.User
import io.nearpay.terminalsdk.listeners.failures.VerifyMobileFailure

interface VerifyMobileListener {
    fun onVerifyMobileSuccess(user: User)
    fun onVerifyMobileFailure(verifyMobileFailure: VerifyMobileFailure)
}