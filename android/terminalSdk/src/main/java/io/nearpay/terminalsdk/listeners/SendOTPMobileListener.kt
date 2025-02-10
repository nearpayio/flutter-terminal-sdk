package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.OtpResponse
import io.nearpay.terminalsdk.listeners.failures.OTPMobileFailure

interface SendOTPMobileListener {
    fun onSendOTPMobileSuccess(otpResponse: OtpResponse)
    fun onSendOTPMobileFailure(otpMobileFailure: OTPMobileFailure)
}