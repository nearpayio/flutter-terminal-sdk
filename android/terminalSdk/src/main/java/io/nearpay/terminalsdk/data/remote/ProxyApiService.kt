package io.nearpay.terminalsdk.data.remote

import io.nearpay.softpos.data.remote.HealthCheckServiceApi
import io.nearpay.terminalsdk.data.dto.PairingData
import retrofit2.http.DELETE
import retrofit2.http.GET

interface ProxyServiceApi : HealthCheckServiceApi {

    @GET("v2/proxy/terminal/get-pairing-code/")
    suspend fun requestPairingData(): PairingData

    @DELETE("v2/proxy/terminal/room")
    suspend fun forgetProxyRoom()

}