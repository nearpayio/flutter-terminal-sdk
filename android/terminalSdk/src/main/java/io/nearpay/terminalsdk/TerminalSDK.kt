package io.nearpay.terminalsdk

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.util.Log
import androidx.core.content.ContextCompat
import io.nearpay.softpos.library.ProvisionCallBack
import io.nearpay.softpos.library.Country as ReaderCoreCountry
import io.nearpay.terminalsdk.data.dto.Country as SDKCountry
import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.softpos.utils.Environment
import io.nearpay.softpos.utils.NearPayError
import io.nearpay.terminalsdk.data.dto.EmailLogin
import io.nearpay.terminalsdk.data.dto.JWTLoginData
import io.nearpay.terminalsdk.data.dto.LoginData
import io.nearpay.terminalsdk.data.dto.LoginUser
import io.nearpay.terminalsdk.data.dto.MobileLogin
import io.nearpay.terminalsdk.data.dto.PermissionStatus
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.local.EncryptedPreferencesGenerator
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.remote.PosServiceApi
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.TerminalCoreNetwork
import io.nearpay.terminalsdk.data.usecases.JWTLoginUseCase
import io.nearpay.terminalsdk.data.usecases.RefreshPosTokenUseCase
import io.nearpay.terminalsdk.data.usecases.SendOTPEmailUseCase
import io.nearpay.terminalsdk.data.usecases.SendOTPMobileUseCase
import io.nearpay.terminalsdk.data.usecases.VerifyEmailUseCase
import io.nearpay.terminalsdk.data.usecases.VerifyMobileUseCase
import io.nearpay.terminalsdk.listeners.JWTLoginListener
import io.nearpay.terminalsdk.listeners.SendOTPEmailListener
import io.nearpay.terminalsdk.listeners.SendOTPMobileListener
import io.nearpay.terminalsdk.listeners.VerifyEmailListner
import io.nearpay.terminalsdk.listeners.VerifyMobileListener
import io.nearpay.terminalsdk.listeners.failures.VerifyMobileFailure
import io.nearpay.terminalsdk.utils.NetworkClientConfig
import io.nearpay.terminalsdk.utils.getAppVersion
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class TerminalSDK private constructor(
    private var activity: Activity,
    private var environment: Environment,
    private var clientKeystore: Int,
    private var clientKeystorePassword: String,
    private var googleCloudProjectNumber: Long,
    private var huaweiSafetyDetectApiKey: String,
    private var country: SDKCountry
) {
    constructor(
        activity: Activity,
        environment: SdkEnvironment,
        clientKeystore: Int,
        clientKeystorePassword: String,
        googleCloudProjectNumber: Long,
        huaweiSafetyDetectApiKey: String,
        country: SDKCountry
    ) : this(
        activity = activity,
        environment = environment.toEnvironment(),
        clientKeystore = clientKeystore,
        clientKeystorePassword = clientKeystorePassword,
        googleCloudProjectNumber = googleCloudProjectNumber,
        huaweiSafetyDetectApiKey = huaweiSafetyDetectApiKey,
        country = country
    ) {

    }

    private val scope = CoroutineScope(Dispatchers.IO)

    private val terminalSharedPreferences = TerminalSharedPreferences(activity, EncryptedPreferencesGenerator)

    private var serverSwitcher: ServerSwitcher<PosServiceApi>

    private lateinit var devicetoken: String;
    private var readerCoreUI: ReaderCoreUI
    private var networkParams: NetworkParams
    private var provisioningDeferred = CompletableDeferred<String>()

    init {
        readerCoreUI = ReaderCoreUI.Builder()
            .activity(activity)  // Pass the current Activity
            .environment(environment)  // Choose the environment
            .clientKeystore(clientKeystore)  // Provide the client keystore resource ID
            .clientKeystorePassword(clientKeystorePassword)  // sandbox client keystore password
            .googleCloudProjectNumber(googleCloudProjectNumber)
            .huaweiSafetyDetectApiKey(huaweiSafetyDetectApiKey)
            .country(country.getCountry())
            .build()
        networkParams = NetworkParams(
            environment = environment,
            country = country
        )


        readerCoreUI.provision(object : ProvisionCallBack {
            override fun onFailure(error: NearPayError) {
                Timber.tag("ProvisionCallBack").d("onFailure: ${error.message}")
                throw Throwable(error.message)
//                execptionDeferred.complete(Throwable(error.message))
//                provisioningDeferred.completeExceptionally(throw Throwable(error.message))
            }

            override fun onSuccess(deviceToken: String) {
                Timber.d("ProvisionCallBack onSuccess in SDK in init function: $deviceToken")
                devicetoken = deviceToken
                provisioningDeferred.complete(deviceToken)
            }

        })

        TerminalSharedPreferences(activity, EncryptedPreferencesGenerator)
        val posNetwork = TerminalCoreNetwork(
            context = activity,
            sharedPreferences = terminalSharedPreferences,
            packageName = activity.packageName,
            appsVersions = mapOf("App-Version" to getAppVersion(activity))
        )
        val networkConfigs =
            when (country.getCountry()) {
                ReaderCoreCountry.SA -> when (environment) {
                    Environment.SANDBOX -> Pair(
                        NetworkClientConfig.SANDBOX_MTLS_1,
                        NetworkClientConfig.SANDBOX_MTLS_2
                    )

                    Environment.PRODUCTION -> Pair(
                        NetworkClientConfig.PRODUCTION_MTLS_1,
                        NetworkClientConfig.PRODUCTION_MTLS_2
                    )
                }

                ReaderCoreCountry.TR -> when (environment) {
                    Environment.SANDBOX -> Pair(
                        NetworkClientConfig.TR_SANDBOX_MTLS_1,
                        NetworkClientConfig.TR_SANDBOX_MTLS_1
                    )

                    Environment.PRODUCTION -> Pair(
                        NetworkClientConfig.TR_PRODUCTION_MTLS_1,
                        NetworkClientConfig.TR_PRODUCTION_MTLS_1
                    )
                }

                ReaderCoreCountry.USA -> {
                    Pair(
                        NetworkClientConfig.SANDBOX_MTLS_1,
                        NetworkClientConfig.SANDBOX_MTLS_2
                    )
                }
            }

        serverSwitcher =
            ServerSwitcher(
                posNetwork.getPosServiceApi(networkConfigs.first),
                posNetwork.getPosServiceApi(networkConfigs.second)
            )
        posNetwork.setServerSwitcher(serverSwitcher)

    }


    private fun getPermissionsStatus(permissions: List<String>): List<PermissionStatus> {
        return permissions.map { permission ->
            PermissionStatus(
                permission = permission,
                isGranted = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED
            )
        }
    }

    private fun provideMobileSendOTPUseCase(): SendOTPMobileUseCase {
        return SendOTPMobileUseCase(serverSwitcher = serverSwitcher)
    }

    private fun provideEmailSendOTPUseCase(): SendOTPEmailUseCase {
        return SendOTPEmailUseCase(
            serverSwitcher = serverSwitcher
        )
    }

    private fun provideVerifyEmailUseCase(): VerifyEmailUseCase {
        return VerifyEmailUseCase(
            serverSwitcher = serverSwitcher,
            sharedPreferences = terminalSharedPreferences,
            networkParams = networkParams,
            readerCoreUI = readerCoreUI
        )
    }

    private fun provideJWTLoginUseCase(): JWTLoginUseCase {
        return JWTLoginUseCase(
            serverSwitcher = serverSwitcher,
            sharedPreferences = terminalSharedPreferences,
            networkParams = networkParams,
            readerCoreUI = readerCoreUI
        )
    }

    private fun provideVerifyMobileUseCase(): VerifyMobileUseCase {
        return VerifyMobileUseCase(
            serverSwitcher = serverSwitcher,
            sharedPreferences = terminalSharedPreferences,
            networkParams = networkParams,
            readerCoreUI = readerCoreUI
        )
    }
    fun sendOTP(mobileLogin: MobileLogin, sendOTPMobileListener: SendOTPMobileListener){
        scope.launch {
            provideMobileSendOTPUseCase()
                .invoke(mobileLogin, sendOTPMobileListener)
        }
    }

    fun sendOTP(emailLogin: EmailLogin, sendOTPEmailListener: SendOTPEmailListener){
        scope.launch {
            provideEmailSendOTPUseCase()
                .invoke(emailLogin, sendOTPEmailListener)
        }
    }

    fun jwtLogin(jwtLoginData: JWTLoginData, jwtLoginListener: JWTLoginListener) {
        scope.launch {
            provideJWTLoginUseCase().invoke(jwtLoginData, jwtLoginListener)
        }
    }


    fun verify(loginData: LoginData, verifyMobileListener: VerifyMobileListener) =
        scope.launch {
            try {
                Timber.tag("verifyMobile").d("inside verify")
                val token = provisioningDeferred.await()
                Timber.tag("verifyMobile").d("token: $token")
                val loginDataWithToken = loginData.copy(deviceToken = token)
                val response = provideVerifyMobileUseCase().invoke(loginDataWithToken, verifyMobileListener)
                Timber.tag("verifyMobile").d("response: $response")
            } catch (e: Exception) {
                Timber.tag("verifyMobile").d("error: ${e.message}")
                verifyMobileListener.onVerifyMobileFailure(VerifyMobileFailure.VerificationFailure(e.message))
            }
        }

    fun verify(loginData: LoginData, verifyEmailListener: VerifyEmailListner) =
        scope.launch {
            val response = provideVerifyEmailUseCase().invoke(loginData, verifyEmailListener)
            Log.d("verifyEmail", "response: $response")
        }

    fun logout(userUUID: String) {
        terminalSharedPreferences.deleteUserFromCache(userUUID)
    }


     fun checkRequiredPermissions(): List<PermissionStatus> {
        val requiredPermissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.NFC,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.READ_PHONE_STATE,
        )
        return getPermissionsStatus(requiredPermissions)
    }

    fun getUsers(): List<Pair<String?, String>> {
        return terminalSharedPreferences.getUserList().map { authResponse ->
            authResponse.user.userUUID to authResponse.user.name
        }
    }


     fun getUserByUUID(uuid: String): User {
         Timber.d("inside getUserByUUID: $uuid")
         val tmpUser: LoginUser?
         runBlocking {
              tmpUser = terminalSharedPreferences.getUserList().find { it.user.userUUID == uuid }?.user.also {
                 terminalSharedPreferences.setActiveUserUUID(it?.userUUID)
                  // if user is found, call the refresh token use case
                  if (it != null) {
                      RefreshPosTokenUseCase(serverSwitcher, terminalSharedPreferences).invoke()
                  }

             }
         }

        return User(
            name = tmpUser?.name,
            email = tmpUser?.email,
            mobile = tmpUser?.mobile,
            userUUID = tmpUser?.userUUID,
            serverSwitcher = serverSwitcher,
            readerCoreUI = readerCoreUI,
            networkParams = networkParams,
            terminalSharedPreferences = terminalSharedPreferences
        )
    }

//    fun getActiveTerminalsDetailsForUser(userUUID: String): List<TerminalConnection> {
//        val terminalUUIDs = terminalSharedPreferences.getActiveTerminalsForUser(userUUID)
//        val terminals = arrayListOf<TerminalConnection>()
//        Timber.d("Terminals in getActiveTerminalsDetailsForUser: $terminals")
//
//        terminalUUIDs.forEach { uuid ->
//            terminalSharedPreferences.getTerminalsListForUser(userUUID).find { it.uuid == uuid }?.let { terminal ->
//                terminals.add(terminal)
//            }
//        }
//        return terminals
//    }

//
    fun getTerminal(activity: Activity, tid: String): Terminal {
        return Terminal(
            networkParams = networkParams,
            terminalSharedPreferences = terminalSharedPreferences,
            tid = tid,
            activity = activity,
//            userUUID = userUUID!!,
            readerCoreUI = readerCoreUI!!
        )
    }

    fun isNfcEnabled(context: Context): Boolean {
        val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(context)
        return nfcAdapter?.isEnabled == true
    }

    fun isWifiEnabled(context: Context): Boolean {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as android.net.wifi.WifiManager
        return wifiManager.isWifiEnabled
    }



    class Builder {
        private var _activity: Activity? = null
        private var _environment: Environment? = null
        private var _clientKeystore: Int = 0
        private var _clientKeystorePassword: String = ""
        private var _googleCloudProjectNumber: Long = 0
        private var _huaweiSafetyDetectApiKey: String = ""
        private var _country: SDKCountry = SDKCountry.SA

        fun activity(activity: Activity): Builder {
            return apply { this._activity = activity }
        }

        fun environment(environment: SdkEnvironment): Builder {
            return apply { this._environment = environment.toEnvironment() }
        }

        fun clientKeystore(clientKeystore: Int): Builder {
            return apply { this._clientKeystore = clientKeystore }
        }

        fun clientKeystorePassword(clientKeystorePassword: String): Builder {
            return apply { this._clientKeystorePassword = clientKeystorePassword }
        }

        fun googleCloudProjectNumber(googleCloudProjectNumber: Long): Builder {
            return apply { this._googleCloudProjectNumber = googleCloudProjectNumber }
        }

        fun huaweiSafetyDetectApiKey(huaweiSafetyDetectApiKey: String): Builder {
            return apply { this._huaweiSafetyDetectApiKey = huaweiSafetyDetectApiKey }
        }

        fun country(country: SDKCountry): Builder {
            return apply { this._country = country }
        }



        fun build(): TerminalSDK {

            return TerminalSDK(
                activity = _activity ?: throw Throwable("${::_activity.name} can not be null"),
                environment = _environment
                    ?: throw Throwable("${::_environment.name} can not be null"),
                clientKeystore = _clientKeystore ?: throw Throwable("${::_clientKeystore.name} can not be null"),
                clientKeystorePassword = _clientKeystorePassword,
                googleCloudProjectNumber = _googleCloudProjectNumber,
                huaweiSafetyDetectApiKey = _huaweiSafetyDetectApiKey,
                country = _country
            ).apply {
                Timber.d("TerminalSDK built in Builder")
            }
        }
        }
    }


