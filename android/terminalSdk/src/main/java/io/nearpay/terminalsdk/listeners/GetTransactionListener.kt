package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.ReceiptsResponse
import io.nearpay.terminalsdk.listeners.failures.GetTransactionFailure

interface GetTransactionListener {
    fun onGetTransactionSuccess(transaction: ReceiptsResponse)
    fun onGetTransactionFailure(error: GetTransactionFailure)
}
