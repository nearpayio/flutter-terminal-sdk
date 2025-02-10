//package io.nearpay.softpos.core.domain.attestationRequestUseCases
//
//import android.content.Context
//import com.auth0.android.jwt.JWT
//import com.google.android.play.core.integrity.IntegrityManagerFactory
//import com.google.android.play.core.integrity.IntegrityTokenRequest
//import com.huawei.hms.support.api.safetydetect.SafetyDetect
//import dagger.hilt.android.qualifiers.ApplicationContext
//import io.nearpay.softpos.core.BuildConfig
//import io.nearpay.terminalsdk.data.dto.AttestationBody
//import io.nearpay.terminalsdk.data.dto.AttestationError
//import io.nearpay.softpos.core.data.remote.ApiResponse
//import io.nearpay.softpos.core.utils.MobileServicesUtils.firstMobileServiceAppeared
//import io.nearpay.softpos.core.utils.managers.PhoneInfoManager.retrieveSimInfo
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//const val OTHERS_ERROR = "neither_google_nor_huawei_were_found"
//
//const val GOOGLE_SOURCE_V2 = "google_v2"
//const val HUAWEI_SOURCE = "huawei"
//const val OTHERS_SOURCE = "others"
//
//class RequestDeviceAttestation @Inject constructor(
//    @ApplicationContext private val context: Context,
//    private val getNonceUseCase: GetNonceUseCase,
//    private val sendAttestationUseCase: SendAttestationUseCase
//) {
//
//    private val scope = CoroutineScope(Dispatchers.IO)
//
//    operator fun invoke() = scope.launch {
//        getNonceUseCase().let {
//            if (it is ApiResponse.Success)
//                requestDeviceIntegrityVerdict(it.items)
//        }
//    }
//
//    private suspend fun requestDeviceIntegrityVerdict(nonceJwt: String) {
//        context.firstMobileServiceAppeared(
//            google = { googlePlayIntegrity(nonceJwt) },
//            huawei = { huaweiSafetyDetect(nonceJwt) },
//            others = { otherDevices(nonceJwt) }
//        )
//    }
//
//    private fun googlePlayIntegrity(nonceJwt: String) = scope.launch {
//        val decodedNonce = JWT(nonceJwt).getClaim("nonce").asString().orEmpty()
//
//        val cloudProjectNumber = BuildConfig.GOOGLE_PLAY_CLOUD_PROJECT_NUMBER.toLong()
//
//        val integrityManager = IntegrityManagerFactory.create(context)
//
//        val integrityTokenRequest = IntegrityTokenRequest.builder()
//            .setCloudProjectNumber(cloudProjectNumber)
//            .setNonce(decodedNonce)
//            .build()
//
//        integrityManager.requestIntegrityToken(integrityTokenRequest)
//            .addOnSuccessListener {
//                val attestationBody = buildAttestationBody(nonceJwt = nonceJwt, source = GOOGLE_SOURCE_V2, safetynetJwt = it.token().orEmpty())
//                scope.launch { sendAttestationUseCase(attestationBody = attestationBody) }
//            }
//            .addOnFailureListener {
//                val attestationBody = buildAttestationBody(nonceJwt = nonceJwt, source = GOOGLE_SOURCE_V2, error = AttestationError(GOOGLE_SOURCE_V2, it.message.orEmpty()))
//                scope.launch { sendAttestationUseCase(attestationBody = attestationBody) }
//            }
//    }
//
//    private fun huaweiSafetyDetect(nonceJwt: String) = scope.launch {
//        val decodedNonce = JWT(nonceJwt).getClaim("nonce").asString().orEmpty()
//
//        val byteArrayDecodedNonce = decodedNonce.toByteArray()
//
//        val apiKey = BuildConfig.HUAWEI_SAFETYDETECT_API_KEY
//
//        val safetyDetect = SafetyDetect.getClient(context)
//
//        safetyDetect.sysIntegrity(byteArrayDecodedNonce, apiKey)
//            .addOnSuccessListener {
//                val jwsString = it.result.replace("==", "")
//                val attestationBody = buildAttestationBody(nonceJwt = nonceJwt, source = HUAWEI_SOURCE, safetynetJwt = jwsString)
//                scope.launch { sendAttestationUseCase(attestationBody = attestationBody) }
//            }
//            .addOnFailureListener {
//                val attestationBody = buildAttestationBody(nonceJwt = nonceJwt, source = HUAWEI_SOURCE, error = AttestationError(HUAWEI_SOURCE, it.message.orEmpty()))
//                scope.launch { sendAttestationUseCase(attestationBody = attestationBody) }
//            }
//    }
//
//    private fun otherDevices(nonceJwt: String) = scope.launch {
//        val attestationBody = buildAttestationBody(nonceJwt, OTHERS_SOURCE, error = AttestationError(OTHERS_SOURCE, OTHERS_ERROR))
//        sendAttestationUseCase(attestationBody = attestationBody)
//    }
//
//    private fun buildAttestationBody(nonceJwt: String, source: String, safetynetJwt: String = "", error: AttestationError? = null): AttestationBody {
//        return AttestationBody(nonceJwt = nonceJwt, source = source, safetynetJwt = safetynetJwt, device = getDeviceInfo(), checks = getCheckInfo(), simInfo = retrieveSimInfo(), checkSumHash = context.getApkChecksum().toString(), error = error)
//    }
//
//}