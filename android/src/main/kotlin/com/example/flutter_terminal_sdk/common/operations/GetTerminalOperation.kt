
package com.example.flutter_terminal_sdk.common.operations

import android.app.Activity
import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler

class GetTerminalOperation(provider: NearpayProvider) : BaseOperation(provider) {
    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {
        val tid = filter.getString("tid") ?: return response(
            ResponseHandler.error("MISSING_TID", "TID is required")
        )

        val activity: Activity = provider.activity
            ?: return response(ResponseHandler.error("NO_ACTIVITY", "Activity reference is null"))

        val terminal = provider.terminalSdk?.getTerminal(activity = activity, tid = tid)
            ?: return response(
                ResponseHandler.error("INVALID_TERMINAL", "No terminal found for TID: $tid")
            )

        val resultData = mapOf(
            "tid" to tid,
            "isReady" to terminal.isTerminalReady(),
            "terminalUUID" to terminal.terminalUUID,
            "uuid" to terminal.terminalUUID,

        )
        response(ResponseHandler.success("Connected to terminal", resultData))

    }
}
