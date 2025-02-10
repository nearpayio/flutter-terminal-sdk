package io.nearpay.terminalsdk.data.usecases

import android.app.Activity
import android.util.Log
import io.nearpay.softpos.library.ProvisionCallBack
import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.softpos.utils.NearPayError
import io.nearpay.terminalsdk.Terminal
import io.nearpay.terminalsdk.data.dto.Auth
import io.nearpay.terminalsdk.data.dto.DeviceToken
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.ConnectTerminalListener
import io.nearpay.terminalsdk.listeners.failures.ConnectTerminalFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume

class ConnectToTerminalUseCase (
    val userUUID: String,
    private val serverSwitcher: ServerSwitcher<PosServiceApi>,
    private val terminalSharedPreferences: TerminalSharedPreferences,
    private val networkParams: NetworkParams,
    private val readerCoreUI: ReaderCoreUI
) {
    private lateinit var devicetoken: String;

    suspend operator fun invoke(
        activity: Activity,
        terminalId: String,
        connectTerminalListener: ConnectTerminalListener
    ) {

        // Wait for provision to complete
        devicetoken = suspendCancellableCoroutine { continuation ->
            readerCoreUI.provision(object : ProvisionCallBack {
                override fun onFailure(error: NearPayError) {
                    Log.d("ProvisionCallBack", "onFailure: $error")
                    if (continuation.isActive)
                        continuation.resume("")
                    else
                        println("Coroutine is not active")
                }

                override fun onSuccess(deviceToken: String) {
                    Log.d("ProvisionCallBack", "onSuccess: $deviceToken")
                    if (continuation.isActive)
                        continuation.resume(deviceToken) // Resume with the actual deviceToken
                    else
                        println("Coroutine is not active")
                }
            })
        }

        val result = safeApiCall {
            serverSwitcher.getApiService().connect(terminalId , DeviceToken(devicetoken))
        }

        when (result) {
            is ApiResponse.Success -> {
                Timber.d("transactionAuth =  ${result.items}")
                terminalSharedPreferences.saveTerminalCredentials(transactionAuth = Auth(
                    result.items.auth.access_token,
                    result.items.auth.refresh_token
                ),
                    tid = terminalId
                )
                terminalSharedPreferences.saveReaderToken(result.items, terminalId)

                Timber.d("val is ${terminalSharedPreferences.getReaderToken(terminalId)}")

                val terminal = Terminal(networkParams, terminalSharedPreferences, activity, terminalId, readerCoreUI)

                withContext(Dispatchers.Main.immediate) {
                    connectTerminalListener.onConnectTerminalSuccess(terminal)
                }
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    connectTerminalListener.onConnectTerminalFailure(ConnectTerminalFailure.Failure(
                        "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                    ))
                }
            }
        }
    }
}