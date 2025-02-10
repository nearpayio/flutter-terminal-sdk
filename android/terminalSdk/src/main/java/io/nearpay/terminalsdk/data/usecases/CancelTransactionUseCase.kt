package io.nearpay.terminalsdk.data.usecases;
import io.nearpay.terminalsdk.data.dto.ID
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher;
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.CancelTransactionListener
import io.nearpay.terminalsdk.listeners.failures.CancelTransactionFailure
import io.nearpay.terminalsdk.utils.runOnMainThread

class CancelTransactionUseCase (
        private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
 ) {

    suspend operator fun invoke(
        id: String,
        cancelTransactionListener: CancelTransactionListener
    ){
        val result = safeApiCall {
            serverSwitcher.getApiService().cancelTransaction(ID(id))
        }
        when(result) {
            is ApiResponse.Success -> {
                runOnMainThread {
                    cancelTransactionListener.onCancelTransactionSuccess(result.items.canceled)
                }
            }
            is ApiResponse.Error -> {
                runOnMainThread {
                    cancelTransactionListener.onCancelTransactionFailure(
                        CancelTransactionFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }

}
