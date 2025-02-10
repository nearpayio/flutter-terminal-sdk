package com.example.flutter_terminal_sdk.common.operations

import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import io.nearpay.terminalsdk.TerminalConnection
import io.nearpay.terminalsdk.listeners.GetTerminalsListener
import io.nearpay.terminalsdk.listeners.failures.GetTerminalsFailure

class GetTerminalListOperation(provider: NearpayProvider) : BaseOperation(provider) {
    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {
        val uuid = filter.getString("uuid") ?: return response(
            ResponseHandler.error("MISSING_UUID", "UUID is required")
        )

        val user = provider.terminalSdk?.getUserByUUID(uuid)
            ?: return response(
                ResponseHandler.error("INVALID_USER", "No user found for UUID: $uuid")
            )
        user.listTerminals(
            page = 1,
            pageSize = 10,
            filter = null,
            listener = object : GetTerminalsListener {
                override fun onGetTerminalsSuccess(terminalsConnection: List<TerminalConnection>) {
                    val mappedTerminals = terminalsConnection.map { t ->
                        mapOf(
                            "name" to t.terminalConnectionData.name,
                            "tid" to t.terminalConnectionData.tid,
                            "uuid" to t.terminalConnectionData.uuid,
                            "busy" to t.terminalConnectionData.busy,
                            "mode" to t.terminalConnectionData.mode,
                            "isLocked" to t.terminalConnectionData.isLocked,
                            "hasProfile" to t.terminalConnectionData.hasProfile,
                            "userUUID" to t.terminalConnectionData.userUUID
                        )
                    }
                    response(
                        ResponseHandler.success(
                            "Terminals fetched",
                            mapOf("terminals" to mappedTerminals)
                        )
                    )
                }

                override fun onGetTerminalsFailure(getTerminalsFailure: GetTerminalsFailure) {
                    response(
                        ResponseHandler.error(
                            "GET_TERMINALS_FAILURE",
                            getTerminalsFailure.toString()
                        )
                    )
                }
            }
        )
    }
}
