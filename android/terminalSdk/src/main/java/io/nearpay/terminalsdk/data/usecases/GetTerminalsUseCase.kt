package io.nearpay.terminalsdk.data.usecases

import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.terminalsdk.TerminalConnection
import io.nearpay.terminalsdk.TerminalConnectionData
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.GetTerminalsListener
import io.nearpay.terminalsdk.listeners.failures.GetTerminalsFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetTerminalsUseCase(
    private val serverSwitcher: ServerSwitcher<PosServiceApi>,
    private val networkParams: NetworkParams,
    private val terminalSharedPreferences: TerminalSharedPreferences,
    private val userUUID: String?,
    private val readerCoreUI: ReaderCoreUI?
) {
//    is_reconciled?:string
//    date?:string
//    from?:string
//    to?:  string
//    customer_reference_number? :string

    suspend operator fun invoke(
        page: Int,
        pageSize: Int,
        filter: String? = null,
        getTerminalsListener: GetTerminalsListener
    ) {

        val result = safeApiCall {
            serverSwitcher.getApiService().getTerminals(
                page = page,
                limit = pageSize,
                filter = filter
            )
        }

         when (result) {
            is ApiResponse.Success -> {
                withContext(Dispatchers.Main.immediate) {
                    if (result.items.terminalsResponse.isEmpty()) {
                        getTerminalsListener.onGetTerminalsFailure(
                            GetTerminalsFailure.FetchingFailure(
                                "No terminals found"
                            )
                        )
                        return@withContext
                    }
                }
                   val terminalConnectionsList = result.items.terminalsResponse.map {
                        TerminalConnection(
                            TerminalConnectionData(
                            it.name ?: "",
                            it.uuid,
                            it.tid ,
                            it.busy,
                            it.mode,
                            it.isLocked,
                            it.hasProfile,
                            userUUID!!,
                            ),
                            serverSwitcher,
                            networkParams,
                            terminalSharedPreferences,
                            readerCoreUI!!
                        )
                    }
                    val terminalConnectionDataList = result.items.terminalsResponse.map {
                        TerminalConnectionData(
                            it.name ?: "",
                            it.uuid,
                            it.tid ,
                            it.busy,
                            it.mode,
                            it.isLocked,
                            it.hasProfile,
                            userUUID!!,
                        )
                    }
                    terminalSharedPreferences.saveTerminalsList(userUUID!!,terminalConnectionDataList)
                withContext(Dispatchers.Main.immediate) {
                    getTerminalsListener.onGetTerminalsSuccess(terminalConnectionsList)
                }
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    getTerminalsListener.onGetTerminalsFailure(
                        GetTerminalsFailure.FetchingFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }

    }

}