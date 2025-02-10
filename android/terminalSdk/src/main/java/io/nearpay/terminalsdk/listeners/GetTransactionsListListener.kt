package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.TransactionsResponse
import io.nearpay.terminalsdk.listeners.failures.GetTransactionsListFailure

interface GetTransactionsListListener {
    fun onGetTransactionsListSuccess(transactionsList: TransactionsResponse)
    fun onGetTransactionsListFailure(error: GetTransactionsListFailure)
}
