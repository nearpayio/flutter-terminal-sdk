package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.TransactionResponse
import io.nearpay.terminalsdk.listeners.failures.RefundTransactionFailure

interface RefundTransactionListener {
    fun onRefundTransactionSuccess(transactionResponse: TransactionResponse)
    fun onRefundTransactionFailure(refundTransactionFailure: RefundTransactionFailure)
}