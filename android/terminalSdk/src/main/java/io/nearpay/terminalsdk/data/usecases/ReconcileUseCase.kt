package io.nearpay.terminalsdk.data.usecases;
import io.nearpay.terminalsdk.data.dto.ReconciliationReceiptsResponse
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher;
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.ReconcileListener
import io.nearpay.terminalsdk.listeners.failures.ReconcileFailure
import io.nearpay.terminalsdk.utils.runOnMainThread
import timber.log.Timber

class ReconcileUseCase (
        private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
 ) {

    suspend operator fun invoke(
        reconcileListener: ReconcileListener
    ) {
        val result = safeApiCall {
            serverSwitcher.getApiService().reconcile()
        }

        when(result) {
            is ApiResponse.Success -> {
                runOnMainThread {
                    reconcileListener.onReconcileSuccess(result.items)
                }
            }
            is ApiResponse.Error -> {
                runOnMainThread {
                    reconcileListener.onReconcileFailure(
                        ReconcileFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }

}
