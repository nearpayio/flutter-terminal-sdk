package io.nearpay.terminalsdk

import android.app.Activity
import android.os.Parcelable
import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.usecases.ConnectToTerminalUseCase
import io.nearpay.terminalsdk.listeners.ConnectTerminalListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize  // Optional, if you still want Parcelable
data class TerminalConnectionData(
    val name: String? = null,
    val tid: String,
    val uuid: String = "",
    val busy: Boolean = false,
    val mode: String,
    val isLocked: Boolean = false,
    val hasProfile: Boolean = false,
    val userUUID: String,
) : Parcelable

class TerminalConnection(
    val terminalConnectionData: TerminalConnectionData,
    val serverSwitcher: ServerSwitcher<PosServiceApi>,
    private val networkParams: NetworkParams,
    private val terminalSharedPreferences: TerminalSharedPreferences,
    private val readerCoreUI : ReaderCoreUI

) {


    private val scope = CoroutineScope(Dispatchers.IO)

    private fun provideConnectToTerminalUseCase(): ConnectToTerminalUseCase {
        return ConnectToTerminalUseCase(
            userUUID = terminalConnectionData.userUUID,
            serverSwitcher = serverSwitcher,
            networkParams = networkParams,
            terminalSharedPreferences = terminalSharedPreferences,
            readerCoreUI = readerCoreUI

        )
    }

    fun connect(activity: Activity, listener: ConnectTerminalListener) {
        scope.launch {
            provideConnectToTerminalUseCase().invoke(activity, terminalConnectionData.tid, listener)
        }
    }
}