//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.softpos.core.data.remote.TransactionServiceApi
//import io.nearpay.softpos.core.data.remote.safeApiCall
//import io.nearpay.softpos.core.di.ServerSwitcher
//import javax.inject.Inject
//import javax.inject.Named
//
//class GetPendingTransactionsTotalUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
//
//    suspend operator fun invoke() = safeApiCall {
//        serverSwitcher.getApiService().getPendingTransactionsTotal()
//    }
//}