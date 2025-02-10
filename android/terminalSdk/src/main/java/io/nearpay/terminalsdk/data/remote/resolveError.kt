package io.nearpay.terminalsdk.data.remote


import io.nearpay.terminalsdk.data.dto.MessageError
import io.nearpay.terminalsdk.data.dto.NiceErrors
import io.nearpay.terminalsdk.utils.AuthenticationException
import io.nearpay.terminalsdk.utils.CoreAction
import io.nearpay.terminalsdk.utils.CoroutineCancelException
import io.nearpay.terminalsdk.utils.InvalidNumberException
import io.nearpay.terminalsdk.utils.NearpayException
import io.nearpay.terminalsdk.utils.NetworkException
import io.nearpay.terminalsdk.utils.NotFoundException
import io.nearpay.terminalsdk.utils.ServerException
import io.nearpay.terminalsdk.utils.TerminalDisconnectedException
import io.nearpay.terminalsdk.utils.TerminalReconcilingException
import io.nearpay.terminalsdk.utils.TerminalUpdatingException
import io.nearpay.terminalsdk.utils.UnexpectedErrorException
import io.nearpay.terminalsdk.utils.ParsingException
import io.nearpay.terminalsdk.utils.UnsupportedAppVersionException
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun resolveError(throwable: Throwable): NearpayException {
    Timber.w(throwable)
    return when (throwable) {
        is HttpException -> resolveHttpException(throwable)
//        is SerializationException -> ParsingResponseException
//        is UnknownHostException -> HostDownException
//        is ConnectException -> NetworkException
//        else -> UnexpectedErrorException
        is SerializationException -> {
//            CoreAction.onSentryAction(throwable)
            ParsingException(throwable)
        }
        is UnknownHostException -> NetworkException(throwable)
        is ConnectException -> NetworkException(throwable)
        is SocketTimeoutException -> NetworkException(throwable)
        is SocketException -> NetworkException(throwable)
        is CancellationException -> CoroutineCancelException(throwable)
        else -> {
//            CoreAction.onSentryAction(throwable)
            UnexpectedErrorException(throwable)
        }
    }
}

private fun resolveHttpException(e: HttpException): NearpayException {
    Timber.d(e)
    Timber.d("the code is ${e.code()}")
    fun HttpException.getServerErrorMsg(): MessageError? {
        return try {
            val errorMessageObject = response()?.errorBody()?.string().orEmpty()
            val json = Json { ignoreUnknownKeys = true; isLenient = true }
            json.decodeFromString<MessageError>(errorMessageObject)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            null
        }
    }

//    fun getLoginType() = TerminalSharedPreferences(CoreAction.getAppContext()).getLoginType()

    fun NearpayException.alsoSendAppAction(): NearpayException {
        CoreAction.onNetworkError(this)
        return this
    }

    fun HttpException.isInvalidMobileError(): Boolean {
        return try {
            val errorBody = response()?.errorBody()?.string().orEmpty()
            val json = Json { ignoreUnknownKeys = true; isLenient = true }
            val errorResponse = json.decodeFromString<MobileErrorResponse>(errorBody)
            errorResponse.message?.mobile != null
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            false
        }
    }

    if (e.code() == 400) {
        // First check if it's a mobile number error
        if (e.isInvalidMobileError()) {
            Timber.d("bruh")
            return InvalidNumberException(e)
        }
        val errorMessage = e.getServerErrorMsg() ?: run {
            NiceErrors.REFUND_AMOUNT_EXCEEDS.messageError
        }
        return ServerException(e, errorMessage)

    }

    if (e.code() == 401) {
        val errorMessage = e.message() ?: run {
            NiceErrors.PARSING_ERROR_MSG_FAILED.messageError
        }
        return AuthenticationException(e)
    }


//        return UnexpectedErrorException(e)

    val errorMessage = e.getServerErrorMsg() ?: run {
//        CoreAction.onSentryAction(e)
        NiceErrors.PARSING_ERROR_MSG_FAILED.messageError
    }

    return when (e.code()) {
        400 -> ServerException(e, errorMessage)
//        401 -> AuthenticationException(e, errorMessage)
        404 -> NotFoundException(e, errorMessage)
        405 -> TerminalDisconnectedException(errorMessage).alsoSendAppAction()
//            {
//            when (getLoginType()) {
//                LoginType.UNDETERMINED,
//                LoginType.JWT ->
//                    AuthenticationException(e, errorMessage)
//
//                LoginType.OTP ->
//                    TerminalDisconnectedException(errorMessage)
//            }.alsoSendAppAction()
//        }
        406 -> TerminalReconcilingException(errorMessage).alsoSendAppAction()
        409 -> UnsupportedAppVersionException(errorMessage).alsoSendAppAction()
        410 -> TerminalUpdatingException(errorMessage).alsoSendAppAction()
        else -> ServerException(e, errorMessage)
    }
}

@Serializable
data class MobileErrorResponse(
    val statusCode: Int? = null,
    val message: MobileError? = null
)

@Serializable
data class MobileError(
    val mobile: List<String>? = null
)
