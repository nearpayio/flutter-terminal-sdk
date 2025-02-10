//package io.nearpay.softpos.core.domain.attestationRequestUseCases
//
//import io.nearpay.softpos.core.data.remote.PosServiceApi
//import io.nearpay.softpos.core.data.remote.safeApiCall
//import io.nearpay.softpos.core.di.ServerSwitcher
//import javax.inject.Inject
//import javax.inject.Named
//
//class GetNonceUseCase @Inject constructor(
//    @Named("PosServerSwitcher") private val serverSwitcher: ServerSwitcher<PosServiceApi>
//) {
//    suspend operator fun invoke() = safeApiCall {
//        serverSwitcher.getApiService().getNonce().nonce
//    }
//}