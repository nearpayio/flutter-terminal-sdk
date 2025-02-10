package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.ReconciliationReceiptsResponse
import io.nearpay.terminalsdk.data.dto.ReconciliationResponse
import io.nearpay.terminalsdk.listeners.failures.GetReconciliationFailure

interface GetReconciliationListener {
    fun onGetReconciliationSuccess(reconciliationResponse: ReconciliationResponse)
    fun onGetReconciliationFailure(error: GetReconciliationFailure)
}