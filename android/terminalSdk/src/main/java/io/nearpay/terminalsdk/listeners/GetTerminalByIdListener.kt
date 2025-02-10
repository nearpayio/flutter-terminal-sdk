package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.TerminalResponse
import io.nearpay.terminalsdk.listeners.failures.GetTerminalByIdFailure

interface GetTerminalByIdListener {
    fun onGetTerminalSuccess(terminalResponse: TerminalResponse)
    fun onGetTerminalFailure(getTerminalByIdFailure: GetTerminalByIdFailure)
}