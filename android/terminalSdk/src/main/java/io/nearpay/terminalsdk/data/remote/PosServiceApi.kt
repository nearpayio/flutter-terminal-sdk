package io.nearpay.terminalsdk.data.remote

import io.nearpay.softpos.data.remote.HealthCheckServiceApi
import io.nearpay.terminalsdk.data.dto.BaselineData
import io.nearpay.terminalsdk.data.dto.BaselineResponseData
import io.nearpay.terminalsdk.data.dto.EmailLogin
import io.nearpay.terminalsdk.data.dto.LoginData
import io.nearpay.terminalsdk.data.dto.AuthResponse
import io.nearpay.terminalsdk.data.dto.DeviceToken
import io.nearpay.terminalsdk.data.dto.JWTLoginData
import io.nearpay.terminalsdk.data.dto.MobileLogin
import io.nearpay.terminalsdk.data.dto.Nonce
import io.nearpay.terminalsdk.data.dto.OtpResponse
import io.nearpay.terminalsdk.data.dto.ReaderAuth
import io.nearpay.terminalsdk.data.dto.TerminalResponse
import io.nearpay.terminalsdk.data.dto.TerminalsResponse
import io.nearpay.terminalsdk.data.dto.TestLoginSDKResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val POS_REFRESH_ENDPOINT = "terminals/v1/refresh"

interface PosServiceApi : HealthCheckServiceApi {

    @Authenticated
    @POST(POS_REFRESH_ENDPOINT)
    suspend fun refreshToken(): AuthResponse

    @GET("v1/cashiers/nonce")
    suspend fun getNonce(): Nonce

    @POST("v1/cashiers/logout")
    suspend fun logout()

    @POST("terminals/v1/login/mobile")
    suspend fun mobileSendOTP(@Body mobile: MobileLogin): OtpResponse

    @POST("v1/cashiers/login/email")
    suspend fun emailSendOTP(@Body email: EmailLogin): OtpResponse

    @POST("v1/cashiers/login/jwt")
    suspend fun loginJWT(@Body jwtLoginData: JWTLoginData): AuthResponse

    @POST("terminals/v1/login/mobile/verify")
    suspend fun verifyMobileLogin(@Body verifyLoginBody: LoginData): AuthResponse

    @POST("v1/cashiers/login/email/verify")
    suspend fun verifyEmailLogin(@Body verifyLoginBody: LoginData): AuthResponse

//    @POST("v1/cashiers/login/sdk")
//    suspend fun loginSDK(@Body loginSdkData: EncryptedData): EncryptedResponseModel

    @Authenticated
    @POST("terminals/v1/terminals/{terminalUuid}/connect")
    suspend fun connect(@Path("terminalUuid") terminalUuid: String,
                        @Body deviceToken: DeviceToken
    ): ReaderAuth

    @POST("v1/cashiers/login/sdk/test-reader")
    suspend fun testReader(): TestLoginSDKResponse

    @POST("v1/cashiers/baseline")
    suspend fun baseline(@Body baselineData: BaselineData): BaselineResponseData


    @Authenticated
    @GET("terminals/v1/terminals/")
    suspend fun getTerminals(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("filter") filter: String?
    ): TerminalsResponse

    @Authenticated
    @GET("v1/cashiers/terminals/{tid}")
    suspend fun getTerminalByTid(
        @Path("tid") tid: String
    ): TerminalResponse


}