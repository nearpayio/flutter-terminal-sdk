package io.nearpay.terminalsdk.data.remote

import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.usecases.RefreshPosTokenUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class PosRefreshTokenAuthenticator(
    private val sharedPreferences: TerminalSharedPreferences,
    private val serverSwitcher: ServerSwitcher<PosServiceApi>,

    ) : Authenticator {

    private fun provideRefreshPosTokenUseCase(): RefreshPosTokenUseCase {
        return RefreshPosTokenUseCase(
            serverSwitcher = serverSwitcher,
            sharedPreferences = sharedPreferences
        )
    }
    override fun authenticate(route: Route?, response: Response): Request? {

        //don't refresh for refresh \_(^.^)_/
        if (response.request.url.toString().contains(POS_REFRESH_ENDPOINT)) {
            return null
        }

        // prevent parallel refresh requests
        synchronized(this) {
        val accessToken = sharedPreferences.getPosAccessToken()
        val alreadyRefreshed = response.request.header("Authorization")?.contains(accessToken, true) == false
        if (alreadyRefreshed) {
            return response.request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }

        runBlocking(Dispatchers.IO) {
            provideRefreshPosTokenUseCase()
                .invoke(authHeader = response.request.header("Authorization").orEmpty())
        }.let { result ->

            return if (result.isSuccess) {
                val newToken = sharedPreferences.getPosAccessToken().orEmpty()
                response.request.newBuilder()
                    .header("Authorization", "Bearer $newToken")
                    .build()

            } else {
//                result.exceptionOrNull()?.let {
//                    resolveRefreshException(it)
//                }
                // TODO: fix handling exception  refresh

                null
                }
            }
        }
    }

}