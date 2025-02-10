//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.softpos.core.data.remote.TransactionServiceApi
//import io.nearpay.softpos.core.data.remote.safeApiCall
//import io.nearpay.softpos.core.di.ServerSwitcher
//import javax.inject.Inject
//import javax.inject.Named
//
//class PostSessionUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
//) {
//    suspend operator fun invoke(
//        id: String
//    ) = safeApiCall {
//        serverSwitcher.getApiService().postSession(id)
//    }
//}