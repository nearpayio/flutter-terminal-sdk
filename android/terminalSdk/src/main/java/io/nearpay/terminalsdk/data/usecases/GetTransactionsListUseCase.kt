package io.nearpay.terminalsdk.data.usecases


import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.GetTransactionsListListener
import io.nearpay.terminalsdk.listeners.failures.GetTransactionsListFailure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber


class GetTransactionsListUseCase  (
 private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
) {

    suspend fun invoke(
        page: Int?,
        pageSize: Int?,
        isReconciled: Boolean?,
        date: Long?,
        startDate: Long?,
        endDate: Long?,
        customerReferenceNumber: String?,
        getTransactionsListListener: GetTransactionsListListener
    ){
        val result = safeApiCall {
            serverSwitcher.getApiService().getTransactions(
                page = page.toString(),
                limit = pageSize.toString(),
                isReconciled = isReconciled.toString(),
                date = date.toString(),
                from = startDate.toString(),
                to = endDate.toString(),
                customerReferenceNumber = customerReferenceNumber
            )
        }
        when (result)
        {
            is ApiResponse.Success -> {
                if (result.items.data.isEmpty()) {
                    withContext(Dispatchers.Main.immediate) {
                        getTransactionsListListener.onGetTransactionsListFailure(
                            GetTransactionsListFailure.GeneralFailure(
                                "No transactions found"
                            )
                        )
                    }
                    return
                }
                Timber.d("GetTransactionsListUseCase: invoke: Success ${result.items}")

                withContext(Dispatchers.Main.immediate) {
                    getTransactionsListListener.onGetTransactionsListSuccess(result.items)
                }
            }

            is ApiResponse.Error -> {
                withContext(Dispatchers.Main.immediate) {
                    getTransactionsListListener.onGetTransactionsListFailure(
                        GetTransactionsListFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }

        }
    }
    }


