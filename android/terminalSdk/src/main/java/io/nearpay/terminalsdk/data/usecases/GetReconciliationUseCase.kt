package io.nearpay.terminalsdk.data.usecases


import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.GetReconciliationListener
import io.nearpay.terminalsdk.listeners.failures.GetReconciliationFailure
import io.nearpay.terminalsdk.utils.runOnMainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class GetReconciliationUseCase (
    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
) {

    suspend operator fun invoke(uuid: String,
                                getReconciliationListener: GetReconciliationListener
    ) = withContext(Dispatchers.IO) {

        val result = safeApiCall {
            serverSwitcher.getApiService().getReconciliation(uuid = uuid)
        }
        when (result) {
            is ApiResponse.Success -> {
                Timber.d("GetReconciliations useCase: invoke: Success ${result.items}")
                runOnMainThread {
                    getReconciliationListener.onGetReconciliationSuccess(result.items)
                }
            }
            is ApiResponse.Error -> {
                runOnMainThread {
                    getReconciliationListener.onGetReconciliationFailure(
                        GetReconciliationFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }
}