package com.example.flutter_terminal_sdk.common.operations

import android.app.Activity
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import io.nearpay.terminalsdk.Terminal
import io.nearpay.terminalsdk.listeners.ConnectTerminalListener
import io.nearpay.terminalsdk.listeners.failures.ConnectTerminalFailure
import com.example.flutter_terminal_sdk.common.NearpayProvider
import io.nearpay.terminalsdk.TerminalConnection
import io.nearpay.terminalsdk.data.dto.TransactionResponse
import io.nearpay.terminalsdk.listeners.GetTerminalByIdListener
import io.nearpay.terminalsdk.listeners.ReadCardListener
import io.nearpay.terminalsdk.listeners.SendTransactionListener
import io.nearpay.terminalsdk.listeners.failures.GetTerminalByIdFailure
import io.nearpay.terminalsdk.listeners.failures.ReadCardFailure
import io.nearpay.terminalsdk.listeners.failures.SendTransactionFailure
import timber.log.Timber
import java.util.UUID

class ConnectTerminalOperation(provider: NearpayProvider) : BaseOperation(provider) {
    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {

        val tid = filter.getString("tid")
            ?: return response(ResponseHandler.error("MISSING_TID", "Terminal TID is required"))

        val terminalUUID = filter.getString("terminalUUID")
            ?: return response(ResponseHandler.error("MISSING_TID", "Terminal TID is required"))

        val userUUID = filter.getString("userUUID")
            ?: return response(ResponseHandler.error("MISSING_USER_UUID", "User UUID is required"))

        val user = provider.terminalSdk?.getUserByUUID(userUUID)
            ?: return response(
                ResponseHandler.error("INVALID_USER", "No user found for UUID: $userUUID")
            )
        val activity: Activity = provider.activity
            ?: return response(ResponseHandler.error("NO_ACTIVITY", "Activity reference is null"))

       user.getTerminalById(terminalUUID, object: GetTerminalByIdListener {
           override fun onGetTerminalSuccess(terminalConnection: TerminalConnection) {
               terminalConnection.connect(activity, object : ConnectTerminalListener {
                   override fun onConnectTerminalSuccess(terminal: Terminal) {
                       val resultData = mapOf(
                           "tid" to tid,
                           "isReady" to terminal.isTerminalReady(),
                           "terminalUUID" to terminal.terminalUUID,
                           "uuid" to terminalUUID

                       )
                       response(ResponseHandler.success("Connected to terminal", resultData))
                   }

                   override fun onConnectTerminalFailure(connectTerminalFailure: ConnectTerminalFailure) {
                       response(ResponseHandler.error("CONNECT_FAILURE", connectTerminalFailure.toString()))
                   }
               })
           }

           override fun onGetTerminalFailure(getTerminalByIdFailure: GetTerminalByIdFailure) {
               response(ResponseHandler.error("GET_TERMINAL_BY_ID_FAILURE", getTerminalByIdFailure.toString()))
           }
       })
    }
}
