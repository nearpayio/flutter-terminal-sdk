package io.nearpay.terminalsdk.utils

import io.nearpay.terminalsdk.data.dto.MessageError
import io.nearpay.terminalsdk.data.dto.NiceErrors
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

sealed class NearpayException(
    override val message: String?,
    open val messageError: MessageError
) : RuntimeException(message)

data class TerminalUpdatingException(
    override val messageError: MessageError
) : NearpayException("TerminalUpdatingException", messageError)

data class TerminalReconcilingException(
    override val messageError: MessageError
) : NearpayException("TerminalReconcilingException", messageError)

data class TerminalDisconnectedException(
    override val messageError: MessageError
) : NearpayException("TerminalDisconnectedException", messageError)

data class SpinConfigMissingException(
    override val messageError: MessageError
) : NearpayException("SpinConfigMissingException", messageError)

data class UnsupportedAppVersionException(
    override val messageError: MessageError
) : NearpayException("UnsupportedAppVersionException", messageError)

data class AuthenticationException(
    val exception: HttpException? = null,
    override val messageError: MessageError = NiceErrors.AUTHENTICATION_ERROR.messageError
) : NearpayException(exception?.message ?: "AuthenticationException", messageError)

data class NotFoundException(
    val exception: HttpException,
    override val messageError: MessageError
) : NearpayException(exception.message ?: "NotFoundException", messageError)

data class ServerException(
    val exception: HttpException,
    override val messageError: MessageError
) : NearpayException(exception.message ?: "ServerException", messageError)

data class UnexpectedHttpException(
    val exception: HttpException,
    override val messageError: MessageError
) : NearpayException(exception.message ?: "UnexpectedHttpException", messageError)

data class ParsingException(
    val exception: SerializationException,
) : NearpayException(
    exception.message ?: "SerializationException", NiceErrors.PARSING_RESPONSE_FAILED.messageError)

data class NetworkException(
    val exception: IOException
) : NearpayException(
    exception.message ?: "IOException", NiceErrors.NETWORK_ERROR.messageError)

data class CoroutineCancelException(
    val exception: CancellationException
) : NearpayException(
    exception.message ?: "CancellationException", NiceErrors.COROUTINE_CANCEL_ERROR.messageError)

data class UnexpectedErrorException(
    val throwable: Throwable
) : NearpayException(
    throwable.message ?: "Something went wrong", NiceErrors.UNEXPECTED_ERROR.messageError)

data class InvalidNumberException(
    val throwable: Throwable
) : NearpayException(
    throwable.message ?: "Something went wrong", NiceErrors.INVALID_MOBILE.messageError)

