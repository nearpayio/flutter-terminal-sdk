//package io.nearpay.install.core.data.remote
//
//import io.nearpay.terminalsdk.data.dto.NiceErrors
//import io.nearpay.terminalsdk.data.dto.ServerErrorMessage
//import kotlinx.serialization.SerializationException
//import retrofit2.HttpException
//import java.io.IOException
//
//class RequestException(
//    override val message: String,
//    val serverErrorMessage: ServerErrorMessage
//) : RuntimeException(message) {
//
//    companion object {
//
//        internal fun authenticationError(
//            exception: HttpException,
//            serverErrorMessage: ServerErrorMessage
//        ) = RequestException(exception.message ?: "authenticationError", serverErrorMessage)
//
//        internal fun notFountError(
//            exception: HttpException,
//            serverErrorMessage: ServerErrorMessage
//        ) = RequestException(exception.message ?: "notFountError", serverErrorMessage)
//
//        internal fun serviceError(
//            exception: HttpException,
//            serverErrorMessage: ServerErrorMessage
//        ) = RequestException(exception.message ?: "serviceError", serverErrorMessage)
//
//        internal fun httpException(
//            exception: HttpException,
//            serverErrorMessage: ServerErrorMessage
//        ) = RequestException(exception.message ?: "httpException", serverErrorMessage)
//
//        fun parsingException(
//            exception: SerializationException
//        ) = RequestException(
//            exception.message ?: "SerializationException", NiceErrors.PARSING_RESPONSE_FAILED.messageError)
//
//        internal fun networkError(
//            exception: IOException
//        ) = RequestException(
//            exception.message ?: "IOException", NiceErrors.NETWORK_ERROR.messageError)
//
//        internal fun unexpectedError(
//            exception: Throwable
//        ) = RequestException(
//            exception.message ?: "Something went wrong", NiceErrors.UNEXPECTED_ERROR.messageError)
//    }
//}