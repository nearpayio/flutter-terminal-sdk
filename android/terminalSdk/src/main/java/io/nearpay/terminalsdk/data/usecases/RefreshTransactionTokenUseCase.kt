//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.terminalsdk.data.dto.Auth
//import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
//import io.nearpay.terminalsdk.data.remote.ApiResponse
//import io.nearpay.terminalsdk.data.remote.PosServiceApi
//import io.nearpay.terminalsdk.data.remote.ServerSwitcher
//import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
//import io.nearpay.terminalsdk.data.remote.safeApiCall
//import retrofit2.HttpException
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.sync.Mutex
//import kotlinx.coroutines.sync.withLock
//import kotlinx.coroutines.withContext
//import kotlinx.coroutines.withTimeout
//import timber.log.Timber
//
//
//class RefreshTransactionTokenUseCase (
//    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
//    private val sharedPreferences: TerminalSharedPreferences
//) {
//
//    private val mutex = Mutex()
//
//    suspend operator fun invoke(authHeader: String? = ""): Result<Unit> = withContext(Dispatchers.IO) {
//        // synchronize to prevent race conditions when multiple threads/coroutines attempt to call refresh endpoint
//        val result = mutex.withLock {
//            val accessToken = sharedPreferences.getPosAccessToken()
//            Timber.d("Access token: $accessToken")
//            Timber.d("Auth header: $authHeader")
//            Timber.d(sharedPreferences.getPosRefreshToken() + "Refresh token")
//            val alreadyRefreshed = authHeader?.contains(accessToken, true)
//            // checks if the request's original JWT token is different than the one persisted
//            // if true then this means that the persisted token has already been refreshed by another thread, and we should use the recently persisted one
////            if (alreadyRefreshed) {
////                Timber.d("Token already refreshed by another thread")
////                return@withContext Result.success(Unit)
////            }
//            refreshPosToken()
//        }
//
//        return@withContext result
//    }
//
//    private suspend fun refreshPosToken(
//        remainingTries: Int = 10
//    ): Result<Unit> {
//        Timber.d("Refreshing POS token")
//        return try {
//            val result = safeApiCall{
//                serverSwitcher.getApiService().refreshToken()
//            }
////            val response = encryptedResponse.decrypt<RefreshPosToken>()
//
//            return when(result) {
//                is ApiResponse.Success -> {
//                    val response = result.items
//                    Timber.d("response = $response ")
//                    Timber.d("calling savePosCredential from RefreshPosTokenUseCase")
//                    sharedPreferences.savePosCredential(
//                        auth = Auth(access_token = response.auth.access_token, refresh_token = response.auth.refresh_token),
//                        loginType = sharedPreferences.getLoginType(),
//                        uuid = "refresh"
//                    )
//
//                    Result.success(Unit)
//                }
//                is ApiResponse.Error -> {
//                    Result.failure(result.requestException)
//                }
//            }
//
//        } catch (throwable: Throwable) {
//            when {
//                throwable is HttpException && throwable.code() in 400..499 ->
//                    Result.failure(throwable)
//
//                remainingTries > 0 ->
//                    refreshPosToken(remainingTries - 1 )
//
//                else ->
//                    Result.failure(throwable)
//            }
//        }
//
//    }
//}