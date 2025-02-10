package io.nearpay.terminalsdk.data.remote

import io.nearpay.terminalsdk.utils.NearpayException


sealed class ApiResponse<out T : Any> {

    data class Success<T : Any>(
        val items: T
    ) : ApiResponse<T>()

    data class Error(
        val requestException: NearpayException
    ) : ApiResponse<Nothing>()
}

