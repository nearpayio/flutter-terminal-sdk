package io.nearpay.terminalsdk.data.usecases


import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.GetTransactionListener
import io.nearpay.terminalsdk.listeners.failures.GetTransactionFailure
import io.nearpay.terminalsdk.utils.runOnMainThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class GetTransactionUseCase (
    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
) {

    suspend operator fun invoke(transactionUuid: String,
                                getTransactionListener: GetTransactionListener
    ) = withContext(Dispatchers.IO) {

       val result = safeApiCall {
            serverSwitcher.getApiService().getTransaction(transactionUuid = transactionUuid)
        }
        Timber.d("transactionUuid: $transactionUuid")
        when (result) {
            is ApiResponse.Success -> {
                if (result.items.receipts.isEmpty()) {
                    withContext(Dispatchers.Main.immediate) {
                        getTransactionListener.onGetTransactionFailure(
                            GetTransactionFailure.GeneralFailure(
                                "No transaction found"
                            )
                        )
                    }
                    return@withContext
                }
                Timber.d("GetTransactionUseCase: invoke: Success ${result.items}")
                runOnMainThread{
                    getTransactionListener.onGetTransactionSuccess(result.items)
                }
            }
            is ApiResponse.Error -> {
                runOnMainThread {
                    getTransactionListener.onGetTransactionFailure(
                        GetTransactionFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }
}