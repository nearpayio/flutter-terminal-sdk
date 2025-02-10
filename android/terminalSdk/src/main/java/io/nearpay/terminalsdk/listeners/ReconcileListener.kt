package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.data.dto.ReconciliationReceiptsResponse
import io.nearpay.terminalsdk.listeners.failures.ReconcileFailure


interface ReconcileListener {
    fun onReconcileSuccess(reconciliationReceiptsResponse : ReconciliationReceiptsResponse)
    fun onReconcileFailure(reconcileFailure: ReconcileFailure)
}