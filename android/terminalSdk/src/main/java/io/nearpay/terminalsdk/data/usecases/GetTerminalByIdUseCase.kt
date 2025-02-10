package io.nearpay.terminalsdk.data.usecases

import io.nearpay.terminalsdk.data.dto.TerminalResponse
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.GetTerminalByIdListener
import io.nearpay.terminalsdk.listeners.failures.GetTerminalByIdFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetTerminalByIdUseCase(
    private val serverSwitcher: ServerSwitcher<PosServiceApi>
) {
    suspend operator fun invoke(
        tid: String,
        getTerminalByIdListener: GetTerminalByIdListener
    ): ApiResponse<TerminalResponse> {

        val result = safeApiCall {
            serverSwitcher.getApiService().getTerminalByTid(tid)
        }

        when (result) {
            is ApiResponse.Success -> {
                withContext(Dispatchers.Main.immediate) {
                    getTerminalByIdListener.onGetTerminalSuccess(result.items)
                }
                return result
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    getTerminalByIdListener.onGetTerminalFailure(
                        GetTerminalByIdFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
                return result
            }
        }
    }
}