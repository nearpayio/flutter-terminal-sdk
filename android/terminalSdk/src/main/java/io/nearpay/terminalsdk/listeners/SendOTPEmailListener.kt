package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.OtpResponse
import io.nearpay.terminalsdk.listeners.failures.OTPEmailFailure

interface SendOTPEmailListener {
    fun onSendOTPEmailSuccess(otpResponse: OtpResponse)
    fun onSendOTPEmailFailure(otpEmailFailure: OTPEmailFailure)
}