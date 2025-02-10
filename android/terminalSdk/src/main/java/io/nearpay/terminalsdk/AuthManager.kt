//package io.nearpay.terminalsdk
//
//import android.app.Activity
//import android.util.Log
//import io.nearpay.softpos.library.Country
//import io.nearpay.softpos.utils.Environment
//import io.nearpay.terminalsdk.data.dto.EmailLogin
//import io.nearpay.terminalsdk.data.dto.LoginData
//import io.nearpay.terminalsdk.data.dto.MobileLogin
//import io.nearpay.terminalsdk.data.local.EncryptedPreferencesGenerator
//import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
//import io.nearpay.terminalsdk.data.remote.PosServiceApi
//import io.nearpay.terminalsdk.data.remote.ServerSwitcher
//import io.nearpay.terminalsdk.data.remote.TerminalCoreNetwork
//import io.nearpay.terminalsdk.data.usecases.SendOTPEmailUseCase
//import io.nearpay.terminalsdk.data.usecases.SendOTPMobileUseCase
//import io.nearpay.terminalsdk.data.usecases.VerifyEmailUseCase
//import io.nearpay.terminalsdk.data.usecases.VerifyMobileUseCase
//import io.nearpay.terminalsdk.di.PosDependenciesProvider
//import io.nearpay.terminalsdk.listeners.SendOTPEmailListener
//import io.nearpay.terminalsdk.listeners.SendOTPMobileListener
//import io.nearpay.terminalsdk.listeners.VerifyEmailListner
//import io.nearpay.terminalsdk.listeners.VerifyMobileListener
//import io.nearpay.terminalsdk.utils.NetworkClientConfig
//import io.nearpay.terminalsdk.utils.getAppVersion
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//
//class AuthManager (
//    private val activity: Activity,
//    private val environment: SdkEnvironment,
//    private val country: Country? = null
//){
//    private val scope = CoroutineScope(Dispatchers.IO)
//    private var serverSwitcher: ServerSwitcher<PosServiceApi>
//    private val terminalSharedPreferences = TerminalSharedPreferences(activity, EncryptedPreferencesGenerator)
//
//    init {
//        TerminalSharedPreferences(activity, EncryptedPreferencesGenerator)
//        val posNetwork = TerminalCoreNetwork(
//            context = activity,
//            sharedPreferences = terminalSharedPreferences,
//            packageName = activity.packageName,
//            appsVersions = mapOf("App-Version" to getAppVersion(activity))
//        )
//        val networkConfigs =
//            when (country) {
//                Country.SA -> when (environment.toEnvironment()) {
//                    Environment.SANDBOX -> Pair(
//                        NetworkClientConfig.SANDBOX_MTLS_1,
//                        NetworkClientConfig.SANDBOX_MTLS_2
//                    )
//
//                    Environment.PRODUCTION -> Pair(
//                        NetworkClientConfig.PRODUCTION_MTLS_1,
//                        NetworkClientConfig.PRODUCTION_MTLS_2
//                    )
//                }
//
//                Country.TR -> when (environment.toEnvironment()) {
//                    Environment.SANDBOX -> Pair(
//                        NetworkClientConfig.TR_SANDBOX_MTLS_1,
//                        NetworkClientConfig.TR_SANDBOX_MTLS_1
//                    )
//
//                    Environment.PRODUCTION -> Pair(
//                        NetworkClientConfig.TR_PRODUCTION_MTLS_1,
//                        NetworkClientConfig.TR_PRODUCTION_MTLS_1
//                    )
//                }
//
//                null -> when (environment.toEnvironment()) {
//                    Environment.SANDBOX -> Pair(
//                        NetworkClientConfig.SANDBOX_MTLS_1,
//                        NetworkClientConfig.SANDBOX_MTLS_2
//                    )
//
//                    Environment.PRODUCTION -> Pair(
//                        NetworkClientConfig.PRODUCTION_MTLS_1,
//                        NetworkClientConfig.PRODUCTION_MTLS_2
//                    )
//                }
//
//                Country.USA -> {
//                    Pair(
//                        NetworkClientConfig.SANDBOX_MTLS_1,
//                        NetworkClientConfig.SANDBOX_MTLS_2
//                    )
//                }
//            }
//
//        serverSwitcher =
//            ServerSwitcher(
//                posNetwork.getPosServiceApi(networkConfigs.first),
//                posNetwork.getPosServiceApi(networkConfigs.second)
//            )
//    }
//
//    private fun provideMobileSendOTPUseCase(): SendOTPMobileUseCase {
//        return SendOTPMobileUseCase(serverSwitcher = serverSwitcher)
//    }
//
//    private fun provideEmailSendOTPUseCase(): SendOTPEmailUseCase {
//        return SendOTPEmailUseCase(
//            serverSwitcher = serverSwitcher
//        )
//    }
//
//    private fun provideVerifyEmailUseCase(): VerifyEmailUseCase {
//        return VerifyEmailUseCase(
//            serverSwitcher = serverSwitcher,
//            sharedPreferences = terminalSharedPreferences
//        )
//    }
//
////    private fun provideVerifyMobileUseCase(): VerifyMobileUseCase {
////        return VerifyMobileUseCase(
////            serverSwitcher = serverSwitcher,
////            sharedPreferences = terminalSharedPreferences
////        )
////    }
//    fun sendOTP(mobileLogin: MobileLogin, sendOTPMobileListener: SendOTPMobileListener){
//        scope.launch {
//            provideMobileSendOTPUseCase()
//                .invoke(mobileLogin, sendOTPMobileListener)
//        }
//    }
//
//    fun sendOTP(emailLogin: EmailLogin, sendOTPEmailListener: SendOTPEmailListener){
//        scope.launch {
//            provideEmailSendOTPUseCase()
//                .invoke(emailLogin, sendOTPEmailListener)
//        }
//    }
//
//
//    fun verify(loginData: LoginData, verifyMobileListener: VerifyMobileListener) =
//        scope.launch {
//            val response = provideVerifyMobileUseCase().invoke(loginData, verifyMobileListener)
//            Log.d("verifyMobile", "response: $response")
//        }
//
//    fun verify(loginData: LoginData, verifyEmailListener: VerifyEmailListner) =
//        scope.launch {
//            val response = provideVerifyEmailUseCase().invoke(loginData, verifyEmailListener)
//            Log.d("verifyEmail", "response: $response")
//        }
//
////    fun verify() jwt
//
//
//}
