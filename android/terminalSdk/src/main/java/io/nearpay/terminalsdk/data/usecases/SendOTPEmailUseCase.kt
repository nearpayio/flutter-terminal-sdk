package io.nearpay.terminalsdk.data.usecases

import io.nearpay.terminalsdk.data.dto.EmailLogin
import io.nearpay.terminalsdk.data.dto.OtpResponse
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.SendOTPEmailListener
import io.nearpay.terminalsdk.listeners.failures.OTPEmailFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class SendOTPEmailUseCase(
    private val serverSwitcher: ServerSwitcher<PosServiceApi>
) {

    suspend fun invoke(
        emailLogin: EmailLogin,
        sendOTPEmailListener: SendOTPEmailListener
    ){

        val result = safeApiCall {
            serverSwitcher.getApiService().emailSendOTP(emailLogin)
        }

        when (result) {
            is ApiResponse.Success -> {
                withContext(Dispatchers.Main.immediate) {
                    sendOTPEmailListener.onSendOTPEmailSuccess(result.items)
                }
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    sendOTPEmailListener.onSendOTPEmailFailure(OTPEmailFailure.AuthenticationFailure(
                        "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                    ))
                }
            }
        }
    }


}