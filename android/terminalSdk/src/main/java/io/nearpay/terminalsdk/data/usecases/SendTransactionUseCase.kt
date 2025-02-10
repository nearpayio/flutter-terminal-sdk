package io.nearpay.terminalsdk.data.usecases;
import io.nearpay.terminalsdk.data.dto.Location;
import io.nearpay.terminalsdk.data.dto.ReaderEnvelope;
import io.nearpay.terminalsdk.data.dto.SendTransaction
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher;
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.SendTransactionListener
import io.nearpay.terminalsdk.listeners.failures.SendTransactionFailure
import io.nearpay.terminalsdk.utils.runOnMainThread

class SendTransactionUseCase (
        private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
 ) {

    suspend operator fun invoke(
        readerEnvelope: ReaderEnvelope,
        location: Location,
        sendTransactionListener: SendTransactionListener
    ){
        val result = safeApiCall {
            serverSwitcher.getApiService().sendTransaction(SendTransaction(readerEnvelope, location))
        }
         when(result) {
            is ApiResponse.Success -> {
                runOnMainThread{
                    sendTransactionListener.onSendTransactionSuccess(result.items)
                }
            }
            is ApiResponse.Error -> {
                runOnMainThread{
                    sendTransactionListener.onSendTransactionFailure(
                        SendTransactionFailure.TransactionFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }

}
