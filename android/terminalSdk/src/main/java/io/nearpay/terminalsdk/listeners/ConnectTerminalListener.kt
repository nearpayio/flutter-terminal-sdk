package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.Terminal
import io.nearpay.terminalsdk.data.dto.ReaderAuth
import io.nearpay.terminalsdk.data.dto.TerminalResponse
import io.nearpay.terminalsdk.listeners.failures.ConnectTerminalFailure

interface ConnectTerminalListener {
    fun onConnectTerminalSuccess(terminal: Terminal)
    fun onConnectTerminalFailure(connectTerminalFailure: ConnectTerminalFailure)
}