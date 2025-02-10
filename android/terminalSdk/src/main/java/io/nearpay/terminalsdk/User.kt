package io.nearpay.terminalsdk

import android.app.Activity
import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.usecases.GetTerminalsUseCase
import io.nearpay.terminalsdk.listeners.GetTerminalsListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class User internal constructor(
    val name: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    var userUUID: String? = null,
    private val readerCoreUI: ReaderCoreUI? = null,
    private val serverSwitcher: ServerSwitcher<PosServiceApi>,
    private val networkParams: NetworkParams,
    private val terminalSharedPreferences: TerminalSharedPreferences
) {
    private val scope = CoroutineScope(Dispatchers.IO)


    private fun provideGetTerminalsUseCase(): GetTerminalsUseCase {
        return GetTerminalsUseCase(
            serverSwitcher = serverSwitcher,
            networkParams = networkParams,
            terminalSharedPreferences = terminalSharedPreferences,
            userUUID = userUUID,
            readerCoreUI = readerCoreUI
        )
    }
    fun listTerminals(
        page: Int,
        pageSize: Int,
        filter: String? = null,
        listener: GetTerminalsListener
    ) {
//        if (!isActiveSession()) {
//            listener.onGetTerminalsFailure(GetTerminalsFailure.GeneralFailure("User session is not active"))
//            return
//        }
        scope.launch {
            provideGetTerminalsUseCase()
                .invoke(
                    page = page,
                    pageSize = pageSize,
                    filter = filter,
                    getTerminalsListener = listener
                    )
        }
    }





// TODO   Scenario: the user can list terminals then add another terminal from
//    the dashboard then try to call getTerminal(tid) to get the terminal
//    that was just added and it will always fail to get the terminal because the
//    terminalSharedPreferences is not updated with the new terminal that was just added.

//    fun refreshTerminalsList(){
//        terminalSharedPreferences.clearTerminals()
//    }
}