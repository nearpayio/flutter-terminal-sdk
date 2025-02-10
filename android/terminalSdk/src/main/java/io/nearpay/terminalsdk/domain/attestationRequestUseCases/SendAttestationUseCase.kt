//package io.nearpay.softpos.core.domain.attestationRequestUseCases
//
//import io.nearpay.softpos.core.crypto.CryptoManager
//import io.nearpay.terminalsdk.data.dto.AttestationBody
//import io.nearpay.softpos.core.data.local.NearPaySharedPreferences
//import io.nearpay.softpos.core.data.remote.PosServiceApi
//import io.nearpay.softpos.core.data.remote.safeApiCall
//import io.nearpay.softpos.core.di.ServerSwitcher
//import kotlinx.serialization.encodeToString
//import kotlinx.serialization.json.Json
//import javax.inject.Inject
//import javax.inject.Named
//
//class SendAttestationUseCase @Inject constructor(
//    @Named("PosServerSwitcher") private val serverSwitcher: ServerSwitcher<PosServiceApi>,
//    private val sharedPreferences: NearPaySharedPreferences
//) {
//
//    suspend operator fun invoke(attestationBody: AttestationBody) = safeApiCall {
//        val serializedAttestation = Json.encodeToString(attestationBody).toByteArray()
//        val encryptedAttestation = CryptoManager.encryptAES(serializedAttestation, sharedPreferences.getTA())
//
//        serverSwitcher.getApiService().postAttestation(encryptedAttestation)
//    }
//
//}