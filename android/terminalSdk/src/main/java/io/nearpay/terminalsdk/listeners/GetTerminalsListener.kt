package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.TerminalConnection
import io.nearpay.terminalsdk.data.dto.TerminalsResponse
import io.nearpay.terminalsdk.listeners.failures.GetTerminalsFailure

interface GetTerminalsListener {
    fun onGetTerminalsSuccess(terminalsConnection: List<TerminalConnection>)
    fun onGetTerminalsFailure(getTerminalsFailure: GetTerminalsFailure)
}