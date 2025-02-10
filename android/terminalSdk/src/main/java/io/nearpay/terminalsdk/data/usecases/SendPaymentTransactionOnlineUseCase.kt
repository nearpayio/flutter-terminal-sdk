//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.softpos.core.crypto.CryptoManager
//import io.nearpay.terminalsdk.data.dto.Confirmation
//import io.nearpay.terminalsdk.data.dto.EncryptedResponseModel
//import io.nearpay.softpos.core.data.dto.LocalPaymentState
//import io.nearpay.softpos.core.data.dto.OnlineReceipt
//import io.nearpay.terminalsdk.data.dto.Payment
//import io.nearpay.terminalsdk.data.dto.PerformanceItem
//import io.nearpay.terminalsdk.data.dto.TransactionReceipt
//import io.nearpay.softpos.core.data.local.room.NearpayDao
//import io.nearpay.softpos.core.data.remote.ApiResponse
//import io.nearpay.softpos.core.data.remote.TransactionServiceApi
//import io.nearpay.softpos.core.data.remote.resolveError
//import io.nearpay.softpos.core.di.ServerSwitcher
//import io.nearpay.softpos.core.transaction.TransactionPerformance
//import io.nearpay.softpos.core.transaction.TransactionPerformance.PerformanceType.MOBILE_RECEIVE_RESPONSE
//import io.nearpay.softpos.core.transaction.TransactionPerformance.PerformanceType.MOBILE_SEND_TLV
//import io.nearpay.softpos.core.utils.hexStringToByteArray
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.launch
//import kotlinx.serialization.json.Json
//import okhttp3.Headers
//import retrofit2.HttpException
//import retrofit2.Response
//import timber.log.Timber
//import java.io.IOException
//import java.nio.charset.StandardCharsets
//import java.util.Base64
//import javax.inject.Inject
//import javax.inject.Named
//
//class SendPaymentTransactionOnlineUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>,
//    private val nearpayDao: NearpayDao
//) {
//
//    private val json = Json { isLenient = true; ignoreUnknownKeys = true }
//
//    suspend operator fun invoke(
//        payment: Payment
//    ): Pair<ApiResponse<OnlineReceipt>, Confirmation> {
//
//        val totalTries = 3
//
//        return executor(
//            paymentUuid = payment.uuid,
//            remainingTries = totalTries
//        ) { remainingTries ->
//
//            var timeout = "15000"
//
//            if (remainingTries == totalTries) { // the first time
//                timeout = "30000"
//                TransactionPerformance.addItem(MOBILE_SEND_TLV, System.currentTimeMillis())
//            }
//
//            serverSwitcher.getApiService().sendTransaction(
//                transactions = payment,
//                connectTimeoutMillis = "10000",
//                readTimeoutMillis = timeout,
//                writeTimeoutMillis = timeout
//            )
//        }
//    }
//
//    private suspend fun executor(
//        paymentUuid: String,
//        remainingTries: Int,
//        call: suspend (Int) -> Response<EncryptedResponseModel>
//    ): Pair<ApiResponse<OnlineReceipt>, Confirmation> {
//
//        var isReceived = false
//        var isDecrypted = false
//        var isParsed = false
//
//        fun handleResponseHeader(headers: Headers) {
//            try {
//                val performanceHeader = headers["performance"].orEmpty()
//                val performanceByteDecoded = Base64.getDecoder().decode(performanceHeader)
//                val performanceString = String(performanceByteDecoded, StandardCharsets.UTF_8)
//
//                val performanceList = json.decodeFromString<List<PerformanceItem>>(performanceString)
//                TransactionPerformance.addAll(performanceList)
//
//                Timber.i("header performance: $performanceString")
//            } catch (throwable: Throwable) {
//                Timber.d(throwable.message)
//            }
//        }
//
//        fun handleResponse(response: Response<EncryptedResponseModel>): EncryptedResponseModel {
//            val body = response.body()
//
//            TransactionPerformance.addItem(MOBILE_RECEIVE_RESPONSE, System.currentTimeMillis())
//            isReceived = true
//            Timber.i("response received: $response")
//
//            val encryptedResponseModel = if (response.isSuccessful && body is EncryptedResponseModel)
//                body
//            else
//                throw HttpException(response)
//
//            CoroutineScope(Dispatchers.Default).launch {
//                handleResponseHeader(response.headers())
//                this.cancel()
//            }
//
//            return encryptedResponseModel
//        }
//
//        fun decryptResponseBody(encryptedResponse: EncryptedResponseModel): String {
//            val decryptedResponse = CryptoManager.decryptResponse(
//                encryptedData = encryptedResponse.encryptedData.hexStringToByteArray(),
//                keyAES = Base64.getDecoder().decode(encryptedResponse.key),
//                iv = encryptedResponse.iv.hexStringToByteArray()
//            )
//
//            isDecrypted = true
//            Timber.i("response decrypted: $decryptedResponse")
//
//            return decryptedResponse
//        }
//
//        fun parseDecryptedBody(response: String): List<TransactionReceipt> {
//            val receipt = json.decodeFromString<List<TransactionReceipt>>(response)
//
//            isParsed = true
//            Timber.i("response parsed: $receipt")
//
//            return receipt
//        }
//
//        fun getConfirmation() = Confirmation(
//            transaction_uuid = paymentUuid,
//            received = isReceived,
//            decrypted = isDecrypted,
//            parsed = isParsed
//        )
//
//        try {
//            val response = call(remainingTries)
//            val encryptedResponse = handleResponse(response)
//            val decryptedResponse = decryptResponseBody(encryptedResponse)
//            val receipt = parseDecryptedBody(decryptedResponse)
//
//            nearpayDao.updateLocalPayment(receipts = receipt, localPaymentState = LocalPaymentState.RECEIVED)
//            return Pair(
//                ApiResponse.Success(
//                    OnlineReceipt(
//                        //if not found throw will handle in catch block
//                        primaryTransactionReceipt = receipt.first { !it.is_reversed },
//                        reversedTransactionReceipt = receipt.firstOrNull { it.is_reversed }
//                    )
//                ),
//                getConfirmation()
//            )
//
//        } catch (throwable: Throwable) {
//            Timber.d(
//                "send payment online failed:\n" +
//                        "- received: $isReceived\n" +
//                        "- decrypted: $isDecrypted\n" +
//                        "- parsed: $isParsed\n" +
//                        "- throwable: $throwable"
//            )
//
//            if (throwable is IOException)
//                nearpayDao.updateLocalPayment(LocalPaymentState.UNRECEIVED)
//
//            return if (!isReceived && remainingTries > 0) {
//                executor(
//                    paymentUuid = paymentUuid,
//                    remainingTries = remainingTries - 1,
//                    call = call
//                )
//            } else {
//                Pair(
//                    ApiResponse.Error(resolveError(throwable)),
//                    getConfirmation()
//                )
//            }
//        }
//    }
//}