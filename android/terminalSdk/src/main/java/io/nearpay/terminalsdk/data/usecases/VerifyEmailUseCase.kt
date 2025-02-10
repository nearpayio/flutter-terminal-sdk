package io.nearpay.terminalsdk.data.usecases

import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.terminalsdk.User
import io.nearpay.terminalsdk.data.dto.LoginData
import io.nearpay.terminalsdk.data.dto.AuthResponse
import io.nearpay.terminalsdk.data.dto.LoginUser
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.VerifyEmailListner
import io.nearpay.terminalsdk.listeners.failures.VerifyEmailFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

import java.util.UUID

class VerifyEmailUseCase (
    private val serverSwitcher: ServerSwitcher<PosServiceApi>,
    private val sharedPreferences: TerminalSharedPreferences,
    private val networkParams: NetworkParams,
    private val readerCoreUI: ReaderCoreUI,
) {

    suspend operator fun invoke(
        verifyLoginBody: LoginData,
        verifyEmailListner: VerifyEmailListner
    ){

        val result = safeApiCall {
            serverSwitcher.getApiService().verifyEmailLogin(verifyLoginBody)
        }
         when(result) {
            is ApiResponse.Success -> {
                val loginUser = LoginUser(result.items.user.name, result.items.user.email, result.items.user.mobile, UUID.randomUUID().toString())
                Timber.d("LoginUser: ${loginUser.email}")
                val userAuthResponse =
                    AuthResponse(loginUser, result.items.auth)
                sharedPreferences.saveUserLogin(userAuthResponse)

                val user = User(
                    loginUser.name,
                    loginUser.email,
                    loginUser.mobile,
                    loginUser.userUUID,
                    networkParams = networkParams,
                    terminalSharedPreferences = sharedPreferences,
                    readerCoreUI = readerCoreUI,
                    serverSwitcher = serverSwitcher
                )

                withContext(Dispatchers.Main.immediate) {
                    verifyEmailListner.onVerifyEmailSuccess(user)
                }
            }
            is ApiResponse.Error -> {
                Timber.d("Error: ${result.requestException}")
                withContext(Dispatchers.Main.immediate) {
                    verifyEmailListner.onVerifyEmailFailure(
                        VerifyEmailFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }
}