package io.nearpay.terminalsdk.data.remote

import androidx.annotation.Keep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

@Keep
suspend inline fun <T : Any> safeApiCall(
    crossinline call: suspend () -> T
): ApiResponse<T> {
    return try {
        Timber.d("safeApiCall making api call...")
        val response = withContext(Dispatchers.IO) {
            call()
        }
        ApiResponse.Success(response)
    } catch (throwable: Throwable) {
        Timber.e("safeApiCall error ${throwable.javaClass.simpleName}")
        Timber.e("safeApiCall error ${throwable.message}")
        Timber.e("safeApiCall error ${throwable.localizedMessage}")
        Timber.e("safeApiCall error ${throwable}")
        ApiResponse.Error(resolveError(throwable))
    }
}