package io.nearpay.terminalsdk.data.remote

import android.content.Context
import com.auth0.android.jwt.JWT
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.usecases.RefreshPosTokenUseCase
import io.nearpay.terminalsdk.utils.NetworkClientConfig
import io.nearpay.terminalsdk.utils.getAnnotation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.Dns
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber
import java.io.File
import java.net.InetAddress
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.time.Instant
import java.util.Collections
import java.util.Timer
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

internal interface ITerminalCoreNetwork {

    fun getPosServiceApi(networkClientConfig: NetworkClientConfig): PosServiceApi
}

internal class TerminalCoreNetwork(
    private val context: Context,
    private val sharedPreferences: TerminalSharedPreferences,
    private val packageName: String,
    private val appsVersions: Map<String, String>,
//    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
) : ITerminalCoreNetwork {

    companion object {

        private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB

        internal const val CONNECT_TIMEOUT_HEADER = "CONNECT_TIMEOUT_HEADER"
        internal const val READ_TIMEOUT_HEADER = "READ_TIMEOUT_HEADER"
        internal const val WRITE_TIMEOUT_HEADER = "WRITE_TIMEOUT_HEADER"

        internal const val CONNECT_TIMEOUT_IN_Millis = 10 * 1000
        internal const val READ_TIMEOUT_IN_Millis = 15 * 1000
        internal const val WRITE_TIMEOUT_IN_Millis = 15 * 10000
    }

    private lateinit var serverSwitcher: ServerSwitcher<PosServiceApi>

    private val provideTimeoutInterceptor: Interceptor
        get() {
            return Interceptor { chain ->
                val request = chain.request()

                val connectNew = request.header(CONNECT_TIMEOUT_HEADER)
                val readNew = request.header(READ_TIMEOUT_HEADER)
                val writeNew = request.header(WRITE_TIMEOUT_HEADER)

                val connectTimeout =
                    if (connectNew.isNullOrBlank()) chain.connectTimeoutMillis()
                    else Integer.valueOf(connectNew)

                val readTimeout =
                    if (readNew.isNullOrBlank()) chain.readTimeoutMillis()
                    else Integer.valueOf(readNew)

                val writeTimeout =
                    if (writeNew.isNullOrBlank()) chain.writeTimeoutMillis()
                    else Integer.valueOf(writeNew)

                chain
                    .withConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                    .withReadTimeout(readTimeout, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                    .proceed(request)
            }
        }

    private val provideHttpLoggingInterceptor: HttpLoggingInterceptor
        get() {
            return HttpLoggingInterceptor { Timber.i(it) }.setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
        }

    private fun Request.Builder.addHeaderToken(request: Request): Request.Builder {
//        if (request.url.toString().contains(POS_REFRESH_ENDPOINT)){
//            Timber.d("Refresh endpoint is called: ${sharedPreferences.getPosRefreshToken()}")
//        } else {
//            Timber.d("POS Refresh Token: ${sharedPreferences.getPosRefreshToken()}")
//            Timber.d("Refresh endpoint is not called")
//        }
        val posToken =
            if (request.url.toString().contains(POS_REFRESH_ENDPOINT))
                sharedPreferences.getPosRefreshToken()
            else
                sharedPreferences.getPosAccessToken()

        if (posToken.isNotBlank()) {
            Timber.d("POS Token: $posToken")
            addHeader("Authorization", "Bearer $posToken")
        }


        return this
    }

    private val provideHeaderInterceptor: Interceptor
        get() {
            return Interceptor {
                val requestBuilder = it.request().newBuilder()

                requestBuilder.addHeader("Content-Type", "application/json")
                requestBuilder.addHeader("Package", packageName)
                requestBuilder.addHeaderToken(it.request())

                appsVersions.forEach { version ->
                    requestBuilder.addHeader(version.key, version.value)
                }

                it.proceed(requestBuilder.build())
            }
        }

//    private val authenticator = PosRefreshTokenAuthenticator(sharedPreferences)

    fun setServerSwitcher(serverSwitcher: ServerSwitcher<PosServiceApi>) {
        this.serverSwitcher = serverSwitcher
    }

    private fun providePosAuthenticationInterceptor(
        sharedPreferences: TerminalSharedPreferences
    ) = Interceptor {
        Timber.d("Providing POS Authentication Interceptor")
        var request = it.request()
        Timber.d("Request URL: ${request.url}")
        val endpointNotRequiresAuth = request.getAnnotation(Authenticated::class.java) == null
        val requestIsRefreshEndpoint = request.url.toString().contains(POS_REFRESH_ENDPOINT)

        Timber.d("Endpoint requires auth: $endpointNotRequiresAuth")
        Timber.d("Request is refresh endpoint: $requestIsRefreshEndpoint")

        if (endpointNotRequiresAuth || requestIsRefreshEndpoint) {
            return@Interceptor it.proceed(request)
        }

        val accessToken = sharedPreferences.getPosAccessToken()

        val oneMinute = 60
        val expireTime = try {
            JWT(accessToken).getClaim("exp").asInt()
        } catch (t: Throwable) {
            null
        }

        val currentTime = Instant.now().epochSecond.toInt() + oneMinute
//        val currentTime = 1735199839
        Timber.d("Current Time: $currentTime")
        if (expireTime == null) return@Interceptor it.proceed(request)
        val refreshPosTokenUseCase = RefreshPosTokenUseCase(serverSwitcher, sharedPreferences)
        Timber.d("Expire Time: $expireTime")
        Timber.d("current time > expire time: ${currentTime > expireTime}")
        if (currentTime > expireTime) {
            // Token is about to expire in less than a minute, refresh immediately
            runBlocking(Dispatchers.IO) {
                val authHeader = request.header("Authorization")
                    refreshPosTokenUseCase(authHeader)

            }.let { result ->
                if (result.isSuccess) {
                    val newToken = sharedPreferences.getPosAccessToken()
                    Timber.d("New Token in the providePosAuthenticationInterceptor: $newToken")
                    request = request.newBuilder()
                        .header("Authorization", "Bearer $newToken")
                        .build()
                } else {
                    result.exceptionOrNull()?.let { exception ->
                        Timber.e(exception)
                    }
                }
            }
        }

        return@Interceptor it.proceed(request)

    }
    private val provideCache: Cache
        get() {
            val httpCacheDirectory = File(context.cacheDir.absolutePath, "HttpCache")
            return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
        }

    private fun provideOkHttpClient(networkClientConfig: NetworkClientConfig): OkHttpClient {

        val spec: ConnectionSpec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
            .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
            .cipherSuites(
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                CipherSuite.TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,
                CipherSuite.TLS_ECDHE_RSA_WITH_RC4_128_SHA,
                CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA,
                CipherSuite.TLS_DHE_DSS_WITH_AES_128_CBC_SHA,
                CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA
            )
            .build()

        val certificatePinnerBuilder = CertificatePinner.Builder()
        if (!networkClientConfig.pins.isNullOrEmpty()) {
            for (pin in networkClientConfig.pins!!) {
                certificatePinnerBuilder.add(pin.pattern, pin.pins)
            }
        }
        val certificatePinner = certificatePinnerBuilder.build()
        val clientBuilder = OkHttpClient.Builder()
            .certificatePinner(certificatePinner)
            .connectionSpecs(Collections.singletonList(spec))
            .readTimeout(READ_TIMEOUT_IN_Millis.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(WRITE_TIMEOUT_IN_Millis.toLong(), TimeUnit.MILLISECONDS)
            .connectTimeout(CONNECT_TIMEOUT_IN_Millis.toLong(), TimeUnit.MILLISECONDS)
            .addInterceptor(provideHeaderInterceptor)
            .addInterceptor(provideTimeoutInterceptor)
//            .authenticator(authenticator)
            .addInterceptor(providePosAuthenticationInterceptor(sharedPreferences))
            .cache(provideCache)

        clientBuilder.addInterceptor(provideHttpLoggingInterceptor)
//        if (BuildConfig.DEBUG) {
//        }

        if (networkClientConfig.ip != null) {
            clientBuilder.dns(
                Dns {
                    val inetAddresses: InetAddress =
                        InetAddress.getByAddress(networkClientConfig.ip)
                    return@Dns listOf(inetAddresses)
                }
            )
        }

        if (networkClientConfig.serverCertificate != null
            && networkClientConfig.clientKeystore != null
            && networkClientConfig.clientKeystorePassword != null
        ) {
            val trustManagerFactory =
                getTrustManagerFactory(networkClientConfig.serverCertificate!!)
            val sslSocketFactory = getSSLContext(
                trustManagerFactory,
                networkClientConfig.clientKeystore!!,
                networkClientConfig.clientKeystorePassword!!.toCharArray()
            ).socketFactory

            clientBuilder.sslSocketFactory(
                sslSocketFactory,
                getTrustManagerFactory(networkClientConfig.serverCertificate!!).trustManagers[0] as X509TrustManager
            )

            val hostnameVerifier = HostnameVerifier { hostname, session ->
                hostname.endsWith(".nearpay.io")
            }
            clientBuilder.hostnameVerifier(hostnameVerifier)
        }

        return clientBuilder.build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun provideRetrofit(serverBaseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = true
            explicitNulls = false
        }

        return Retrofit.Builder()
            .baseUrl(serverBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    private fun getSSLContext(
        trustManagerFactory: TrustManagerFactory,
        clientCertificate: Int,
        clientCertificatePassword: CharArray
    ): SSLContext {

        // Load the client certificate and private key from the BKS keystore
        val clientKeyStore = KeyStore.getInstance("bks").apply {
            load(context.resources.openRawResource(clientCertificate), clientCertificatePassword)
        }

        // Create a KeyManager that uses the client certificate and private key
        val keyManagerFactory =
            KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()).apply {
                init(clientKeyStore, clientCertificatePassword)
            }

        // Initialize an SSLContext with the KeyManager and TrustManager
        return SSLContext.getInstance("TLS").apply {
            init(keyManagerFactory.keyManagers, trustManagerFactory.trustManagers, null)
        }
    }

    private fun getTrustManagerFactory(
        serverCertificate: Int
    ): TrustManagerFactory {
        // Load the custom CA certificate
        val cf = CertificateFactory.getInstance("X.509")
        val caInput = context.resources.openRawResource(serverCertificate)
        val ca = caInput.use { cf.generateCertificate(it) }

        // Create a KeyStore containing our trusted CAs
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("ca", ca)
        }

        // Create a TrustManager that trusts the CAs in our KeyStore
        return TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
            init(keyStore)
        }
    }


    override fun getPosServiceApi(networkClientConfig: NetworkClientConfig): PosServiceApi {
        return provideRetrofit(
            serverBaseUrl = networkClientConfig.url,
            okHttpClient = provideOkHttpClient(networkClientConfig)
        ).create(PosServiceApi::class.java)
    }

    fun providePosServiceApiFactory(): (NetworkClientConfig) -> PosServiceApi {
        return { config -> getPosServiceApi(config) }
    }
//
//    private fun provideRetrofit(serverBaseUrl: String, okHttpClient: OkHttpClient): Retrofit {
//        return Retrofit.Builder()
//            .baseUrl(serverBaseUrl)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

//    override fun getPosServiceApi(
//        networkClientConfig: NetworkClientConfig,
//    ): PosServiceApi {
//        return provideRetrofit(
//            serverBaseUrl = networkClientConfig.url,
//            okHttpClient = provideOkHttpClient(networkClientConfig)
//        ).create(PosServiceApi::class.java)
//    }
}