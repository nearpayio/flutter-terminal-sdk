package io.nearpay.terminalsdk.utils

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

suspend fun <T> awaitFastestResponse(deferredList: List<Deferred<Result<T>>>): T? =
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