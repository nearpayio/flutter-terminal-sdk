package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.TransactionResponse
import io.nearpay.terminalsdk.listeners.failures.SendTransactionFailure

interface SendTransactionListener {
    fun onSendTransactionSuccess(transactionResponse: TransactionResponse)
    fun onSendTransactionFailure(sendTransactionFailure: SendTransactionFailure)
}