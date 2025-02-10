package io.nearpay.terminalsdk.data.remote

import io.nearpay.softpos.data.remote.HealthCheckServiceApi
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import java.net.ConnectException

class ServerSwitcher<T : HealthCheckServiceApi>(
    private val server1ServiceApi: T,
    private val server2ServiceApi: T
) {

    companion object {
        const val SERVER_CHECK_CACHE_DURATION = 10 * 60 * 1000

        private var cachedServer: Server? = null
        private var cacheTimestamp: Long = 0
        private val mutex = Mutex()
    }

    suspend fun getApiService(): T = withContext(Dispatchers.IO) {
        val selectedApiService = mutex.withLock {
            when (getFastestServer()) {
                Server.OCI_MTLS -> server1ServiceApi
                Server.GOOGLE -> server2ServiceApi
                else -> server1ServiceApi
            }
        }
        return@withContext selectedApiService
    }

    private suspend fun getFastestServer(): Server? = withContext(Dispatchers.IO) {
        val currentTime = System.currentTimeMillis()
        if (cachedServer != null && (currentTime - cacheTimestamp) < SERVER_CHECK_CACHE_DURATION) {
            return@withContext cachedServer!!
        }

        try {
            val selectedServer = determineFastestServer()

            cachedServer = selectedServer
            cacheTimestamp = System.currentTimeMillis()
            selectedServer
        } catch (throwable: Throwable) {
            throw throwable
        }
    }

    private suspend fun determineFastestServer(): Server? = withTimeoutOrNull(2000) {
        coroutineScope {
            val deferredList = listOf(
                async {
                    runCatching {
                        val response = server1ServiceApi.healthCheck()
                        if (response.isSuccessful) {
                            Server.OCI_MTLS
                        } else {
                            throw RuntimeException("Unsuccessful health check")
                        }
                    }
                },
                async {
                    runCatching {
                        val response = server2ServiceApi.healthCheck()
                        if (response.isSuccessful) {
                            Server.GOOGLE
                        } else {
                            throw RuntimeException("Unsuccessful health check")
                        }
                    }
                }
            )
            try {
                awaitFastestResponse(deferredList)
            } catch (throwable: Throwable) {
                null
            }
        }
    }

    private suspend fun <T> awaitFastestResponse(deferredList: List<Deferred<Result<T>>>): T? =
        supervisorScope {
            val completableDeferred = CompletableDeferred<T>()

            deferredList.forEach { deferred ->
                launch {
                    val result = deferred.await()
                    if (result.isSuccess) {
                        if (!completableDeferred.isCompleted) {
                            completableDeferred.complete(result.getOrThrow())
                            deferredList.forEach { it.cancel() }
                        }
                    } else {
                        val remainingCount = deferredList.count { !it.isCompleted }
                        if (remainingCount == 0) {
                            // All coroutines have failed
                            completableDeferred.completeExceptionally(
                                result.exceptionOrNull()
                                    ?: RuntimeException("All coroutines failed")
                            )
                        }
                    }
                }
            }

            try {
                completableDeferred.await()
            } catch (e: Throwable) {
                null
            }
        }
}

enum class Server {
    OCI_MTLS,
    GOOGLE
}