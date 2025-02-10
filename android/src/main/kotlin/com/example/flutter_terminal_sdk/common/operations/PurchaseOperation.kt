// android/src/main/kotlin/com/example/flutter_terminal_sdk/common/operations/PurchaseOperation.kt

package com.example.flutter_terminal_sdk.common.operations

import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import com.google.gson.Gson
import io.nearpay.terminalsdk.Terminal
import io.nearpay.terminalsdk.data.dto.PaymentScheme
import io.nearpay.terminalsdk.data.dto.TransactionResponse
import io.nearpay.terminalsdk.listeners.ReadCardListener
import io.nearpay.terminalsdk.listeners.SendTransactionListener
import io.nearpay.terminalsdk.listeners.failures.ReadCardFailure
import io.nearpay.terminalsdk.listeners.failures.SendTransactionFailure
import timber.log.Timber

class PurchaseOperation(provider: NearpayProvider) : BaseOperation(provider) {
    private val gson = Gson()

    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {
        // Extract required arguments
        val uuid = filter.getString("uuid")
            ?: return response(ResponseHandler.error("MISSING_UUID", "Terminal uuid is required"))

        val amount = filter.getLong("amount")
            ?: return response(ResponseHandler.error("MISSING_AMOUNT", "Amount is required"))

        val schemeString = filter.getString("scheme")
            ?: "VISA" // Default scheme if not provided

        val transactionUuid = filter.getString("transactionUuid")
            ?: return response(ResponseHandler.error("MISSING_TRANSACTION_UUID", "Transaction UUID is required"))

        // Convert scheme string to PaymentScheme enum
        val scheme = try {
            PaymentScheme.valueOf(schemeString.uppercase())
        } catch (e: IllegalArgumentException) {
            return response(ResponseHandler.error("INVALID_SCHEME", "Invalid payment scheme: $schemeString"))
        }
        Timber.d("Got Terminal Purchase")
        Timber.d(uuid)

        // Retrieve the TerminalSDK instance
        val terminal: Terminal = provider.terminalSdk?.getTerminal(provider.activity!!, uuid)
            ?: return response(ResponseHandler.error("TERMINAL_NOT_FOUND", "Terminal with uuid = $uuid = not found"))
    Timber.d("Got Terminal successfully")
        try {
            // Initiate the purchase process
            terminal.purchase(
                amount = amount,
                scheme = scheme,
                transactionUUID = transactionUuid,
                readCardListener = object : ReadCardListener {
                    override fun onReadCardSuccess() {
                        sendPurchaseEvent(transactionUuid, "cardReadSuccess", "Card read successfully", null)
                    }

                    override fun onReadCardFailure(readCardFailure: ReadCardFailure) {
                        sendPurchaseEvent(transactionUuid, "cardReadFailure", readCardFailure.toString(), null)
                    }

                    override fun onReaderWaiting() {
                        sendPurchaseEvent(transactionUuid, "readerWaiting", null, null)
                    }

                    override fun onReaderReading() {
                        sendPurchaseEvent(transactionUuid, "readerReading", null, null)
                    }

                    override fun onReaderRetry() {
                        sendPurchaseEvent(transactionUuid, "readerRetry", null, null)
                    }

                    override fun onPinEntering() {
                        sendPurchaseEvent(transactionUuid, "pinEntering", null, null)
                    }

                    override fun onReaderFinished() {
                        sendPurchaseEvent(transactionUuid, "readerFinished", null, null)
                    }

                    override fun onReaderError(error: String?) {
                        sendPurchaseEvent(transactionUuid, "readerError", error ?: "Unknown reader error", null)
                    }

                    override fun onReadingStarted() {
                        sendPurchaseEvent(transactionUuid, "readingStarted", null, null)
                    }
                },
                sendTransactionListener = object : SendTransactionListener {
                    override fun onSendTransactionSuccess(transactionResponse: TransactionResponse) {
//                        sendPurchaseEvent(transactionUuid, "sendTransactionSuccess", null, null)
                        val jsonString = gson.toJson(transactionResponse)
                        val map = gson.fromJson(jsonString, Map::class.java) as Map<*, *>
                        sendPurchaseEvent(transactionUuid, "sendTransactionSuccessData", null, map)
                    }

                    override fun onSendTransactionFailure(sendTransactionFailure: SendTransactionFailure) {
                        sendPurchaseEvent(transactionUuid, "sendTransactionFailure", sendTransactionFailure.toString(), null)
                    }
                }
            )
        } catch (e: Exception) {
            // Handle any unexpected exceptions during purchase
            response(ResponseHandler.error("PURCHASE_FAILED", "Purchase failed: ${e.message}"))
        }
    }
    private fun sendPurchaseEvent(
        transactionUuid: String,
        eventType: String,
        message: String?,
        data: Any?
    ) {
        val eventArgs = mutableMapOf<String, Any>(
            "transactionUuid" to transactionUuid,
            "type" to eventType
        )
        message?.let { eventArgs["message"] = it }
        data?.let { eventArgs["data"] = it }

        try {
            provider.methodChannel.invokeMethod("purchaseEvent", eventArgs)
        } catch (e: Exception) {
            // Log the error but do not disrupt the purchase flow
            Timber.e(e, "Failed to send purchase event: $eventType for transactionUuid: $transactionUuid")
        }
    }
}
