//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.softpos.core.data.dto.LocalPaymentState
//import io.nearpay.terminalsdk.data.dto.Reversal
//import io.nearpay.terminalsdk.data.dto.TransactionReceipt
//import io.nearpay.softpos.core.data.local.room.NearpayDao
//import io.nearpay.softpos.core.data.remote.ApiResponse
//import io.nearpay.softpos.core.data.remote.TransactionServiceApi
//import io.nearpay.softpos.core.data.remote.safeApiCall
//import io.nearpay.softpos.core.di.ServerSwitcher
//import javax.inject.Inject
//import javax.inject.Named
//
//class PostReversalUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
//    private val nearpayDao: NearpayDao,
//) {
//
//    private val connectTimeoutMillis = "10000"
//    private val writeTimeoutMillis = "30000"
//    private val readTimeoutMillis = "30000"
//
//    suspend operator fun invoke(
//        reversal: Reversal
//    ): ApiResponse<List<TransactionReceipt>> {
//
//        return safeApiCall {
//            serverSwitcher.getApiService().postReversal(
//                reversal,
//                connectTimeoutMillis,
//                writeTimeoutMillis,
//                readTimeoutMillis
//            )
//        }.let {
//            when (it) {
//                is ApiResponse.Success -> {
//                    if (it.items.isNotEmpty()) {
//                        nearpayDao.updateLocalPayment(
//                            receipts = it.items,
//                            localPaymentState = LocalPaymentState.REVERSED,
//                            transactionUuid = it.items.first().transaction_uuid
//                        )
//                    }
//                    ApiResponse.Success(it.items)
//                }
//
//                is ApiResponse.Error -> {
//                    ApiResponse.Error(it.requestException)
//                }
//            }
//        }
//    }
//}