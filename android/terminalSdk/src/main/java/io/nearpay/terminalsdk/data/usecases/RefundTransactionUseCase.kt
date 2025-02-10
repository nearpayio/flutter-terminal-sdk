package io.nearpay.terminalsdk.data.usecases;
import io.nearpay.terminalsdk.data.dto.Location;
import io.nearpay.terminalsdk.data.dto.ReaderEnvelope;
import io.nearpay.terminalsdk.data.dto.RefundTransaction
import io.nearpay.terminalsdk.data.dto.TransactionResponse
import io.nearpay.terminalsdk.data.remote.ApiResponse
import io.nearpay.terminalsdk.data.remote.ServerSwitcher;
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.remote.safeApiCall
import io.nearpay.terminalsdk.listeners.RefundTransactionListener
import io.nearpay.terminalsdk.listeners.failures.RefundTransactionFailure
import io.nearpay.terminalsdk.utils.runOnMainThread

class RefundTransactionUseCase (
        private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
 ) {

    suspend operator fun invoke(
        readerEnvelope: ReaderEnvelope,
        location: Location,
        refundRRN: String,
        refundTransactionListener: RefundTransactionListener
    ){
        val result = safeApiCall {
            serverSwitcher.getApiService().refundTransaction(RefundTransaction(readerEnvelope, location, refundRRN))
        }
        when(result) {
            is ApiResponse.Success -> {
                runOnMainThread {
                    refundTransactionListener.onRefundTransactionSuccess(result.items)
                }
            }
            is ApiResponse.Error -> {
                runOnMainThread{
                    refundTransactionListener.onRefundTransactionFailure(
                        RefundTransactionFailure.GeneralFailure(
                            "${result.requestException.messageError.error.english}, ${result.requestException.messageError.solution.english}"
                        )
                    )
                }
            }
        }
    }

}
