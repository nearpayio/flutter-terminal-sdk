package io.nearpay.terminalsdk.data.usecases

import io.nearpay.terminalsdk.data.dto.MobileLogin
import io.nearpay.terminalsdk.data.dto.OtpResponse
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.SendOTPMobileListener
import io.nearpay.terminalsdk.listeners.failures.OTPMobileFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

internal class SendOTPMobileUseCase(
    private val serverSwitcher: ServerSwitcher<PosServiceApi>
) {

    suspend fun invoke(
        mobileLogin: MobileLogin,
        sendOTPMobileListener: SendOTPMobileListener
    ) {

        val result = safeApiCall {
            serverSwitcher.getApiService().mobileSendOTP(mobileLogin)
        }

        when (result) {
            is ApiResponse.Success -> {
                withContext(Dispatchers.Main.immediate) {
                    sendOTPMobileListener.onSendOTPMobileSuccess(result.items)
                }
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    Timber.d("Error in sendOTP: ${result.requestException.message}")
                    Timber.d("Error in sendOTP: ${result.requestException.messageError}")
                    Timber.d("Error in sendOTP: ${result.requestException.localizedMessage}")
                    sendOTPMobileListener.onSendOTPMobileFailure(
                        OTPMobileFailure.AuthenticationFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }

}