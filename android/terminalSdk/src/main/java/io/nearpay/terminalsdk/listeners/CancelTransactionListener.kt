package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.IsCanceled
import io.nearpay.terminalsdk.listeners.failures.CancelTransactionFailure

interface CancelTransactionListener {
    fun onCancelTransactionSuccess(isCanceled : Boolean)
    fun onCancelTransactionFailure(cancelTransactionFailure: CancelTransactionFailure)
}