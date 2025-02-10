package io.nearpay.terminalsdk.data.usecases

import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.GetReconciliationListListener
import io.nearpay.terminalsdk.listeners.failures.GetReconciliationListFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class GetReconciliationListUseCase  (
    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
) {

    suspend fun invoke(
        page: Int?,
        pageSize: Int?,
        startDate: Long?,
        endDate: Long?,
        getReconciliationListListener: GetReconciliationListListener
    ){
        val result = safeApiCall {
            serverSwitcher.getApiService().getReconciliations(
                page = page.toString(),
                limit = pageSize.toString(),
                from = startDate.toString(),
                to = endDate.toString(),
            )
        }
        when (result)
        {
            is ApiResponse.Success -> {
                if (result.items.data.isEmpty()) {
                    withContext(Dispatchers.Main.immediate) {
                        getReconciliationListListener.onGetReconciliationListFailure(
                            GetReconciliationListFailure.GeneralFailure(
                                "No reconciliations found"
                            )
                        )
                    }
                    return
                }
                Timber.d("GetTransactionsListUseCase: invoke: Success ${result.items}")

                withContext(Dispatchers.Main.immediate) {
                    getReconciliationListListener.onGetReconciliationListSuccess(result.items)
                }
                ApiResponse.Success(result.items)
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    getReconciliationListListener.onGetReconciliationListFailure(
                        GetReconciliationListFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
                ApiResponse.Error(result.requestException)
            }

        }
    }
}


