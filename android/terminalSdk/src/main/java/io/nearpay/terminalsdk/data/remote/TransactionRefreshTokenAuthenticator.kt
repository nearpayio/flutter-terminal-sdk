//package io.nearpay.terminalsdk.data.remote
//
//
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.runBlocking
//import okhttp3.Authenticator
//import okhttp3.Request
//import okhttp3.Response
//import okhttp3.Route
//
//class TransactionRefreshTokenAuthenticator (
//    private val refreshTransactionTokenUseCase: RefreshTransactionTokenUseCase,
//    private val sharedPreferences: NearPaySharedPreferences
//) : Authenticator {
//
//    override fun authenticate(route: Route?, response: Response): Request? {
//
//        //don't refresh for refresh \_(^.^)_/
//        if (response.request.url.toString().contains(TRANSACTION_REFRESH_ENDPOINT)) {
//            return null
//        }
//
//        // prevent parallel refresh requests
//        synchronized(this) {
//        val accessToken = sharedPreferences.getTransactionAccessToken()
//        val alreadyRefreshed = response.request.header("Authorization")?.contains(accessToken, true) == false
//        if (alreadyRefreshed) {
//            return response.request.newBuilder()
//                .header("Authorization", "Bearer $accessToken")
//                .build()
//        }
//
//        runBlocking(Dispatchers.IO) {
//            refreshTransactionTokenUseCase()
//        }.let { result ->
//
//            return if (result.isSuccess) {
//                val newToken = sharedPreferences.getTransactionAccessToken().orEmpty()
//                response.request.newBuilder()
//                    .header("Authorization", "Bearer $newToken")
//                    .build()
//
//            } else {
//                result.exceptionOrNull()?.let {
//                    resolveRefreshException(it)
//                }
//
//                null
//                }
//            }
//        }
//    }
//
//}