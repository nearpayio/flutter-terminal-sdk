package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.ReconciliationListResponse
import io.nearpay.terminalsdk.listeners.failures.GetReconciliationListFailure

interface GetReconciliationListListener {
    fun onGetReconciliationListSuccess(reconciliationListResponse: ReconciliationListResponse)
    fun onGetReconciliationListFailure(error: GetReconciliationListFailure)
}