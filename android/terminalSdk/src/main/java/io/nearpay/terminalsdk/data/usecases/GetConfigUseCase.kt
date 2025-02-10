//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.softpos.common.remote.ServerSwitcher
//import io.nearpay.softpos.common.remote.response.ApiResponse
//import io.nearpay.terminalsdk.data.dto.NullableTerminalProfile
//import io.nearpay.terminalsdk.data.dto.getTerminalProfileOrNull
//import io.nearpay.terminalsdk.data.dto.loadTerminalProfile
//import io.nearpay.softpos.core.utils.enums.LoadConfigResult
//import io.nearpay.terminalsdk.data.local.NearPaySharedPreferences
//import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
//import java.util.UUID
//import javax.inject.Inject
//import javax.inject.Named
//
//// TODO get configuration from reader core library
//
//
//class GetConfigUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
//    private val nearPaySharedPreferences: NearPaySharedPreferences
//) {
//
//    suspend operator fun invoke(): ApiResponse<LoadConfigResult> {
//
//        val hash = nearPaySharedPreferences.getCurrentConfigHash()
//        val existedTerminalProfile = nearPaySharedPreferences.getTerminalProfile()
//
//        return safeEncryptedApiCall<NullableTerminalProfile> {
//            serverSwitcher.getApiService().getConfig(hash)
//        }.let {
//            when (it) {
//                is ApiResponse.Error -> {
//
//                    if (existedTerminalProfile.loadTerminalProfile(nearPaySharedPreferences))
//                        ApiResponse.Success(LoadConfigResult.USE_EXISTED)
//                    else
//                        ApiResponse.Error(it.requestException)
//
//                }
//                is ApiResponse.Success -> {
//
//                    val updatedTerminalProfile = it.items.getTerminalProfileOrNull()?.apply {
//                        generatedTerminalID = UUID.randomUUID().toString()
//                    }
//
//                    val loadConfigResult = when {
//                        updatedTerminalProfile.loadTerminalProfile(nearPaySharedPreferences) -> LoadConfigResult.USE_UPDATED
//                        existedTerminalProfile.loadTerminalProfile(nearPaySharedPreferences) -> {
//                            if (updatedTerminalProfile == null)
//                                LoadConfigResult.USE_UPDATED
//                            else
//                                LoadConfigResult.USE_EXISTED
//                        }
//                        else -> LoadConfigResult.FAILED
//                    }
//
//                    ApiResponse.Success(loadConfigResult)
//                }
//            }
//        }
//    }
//
//
//}