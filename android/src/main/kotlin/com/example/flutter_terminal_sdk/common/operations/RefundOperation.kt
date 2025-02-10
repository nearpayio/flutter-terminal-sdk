package com.example.flutter_terminal_sdk.common.operations

import io.nearpay.terminalsdk.listeners.failures.RefundTransactionFailure
import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import com.google.gson.Gson
import io.nearpay.terminalsdk.data.dto.PaymentScheme
import io.nearpay.terminalsdk.listeners.ReadCardListener
import io.nearpay.terminalsdk.listeners.RefundTransactionListener
import io.nearpay.terminalsdk.listeners.failures.ReadCardFailure
import timber.log.Timber

class RefundOperation(provider: NearpayProvider) : BaseOperation(provider) {
    private val gson = Gson()
    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {

        val terminalUUID = filter.getString("terminalUUID")
            ?: return response(ResponseHandler.error("MISSING_UUID", "Terminal uuid is required"))

        val transactionUUID = filter.getString("transactionUuid") ?: return response(
            ResponseHandler.error("MISSING_TRANSACTION_UUID", "Transaction UUID is required")
        )
        val refundUUID = filter.getString("refundUuid") ?: return response(
            ResponseHandler.error("MISSING_REFUND_UUID", "Refund UUID is required")
        )
        val amount = filter.getLong("amount") ?: return response(
            ResponseHandler.error("MISSING_AMOUNT", "Amount is required")
        )

        val scheme = filter.getString("scheme")?.let {
            PaymentScheme.valueOf(it.uppercase())
        } ?: PaymentScheme.VISA


        val terminal =
            provider.activity?.let { provider.terminalSdk?.getTerminal(it, terminalUUID) }

        terminal?.refund(
            amount = amount,
            transactionUUID = transactionUUID,
            refundUUID = refundUUID,
            scheme = scheme,
            readCardListener = object : ReadCardListener {
                override fun onReadCardSuccess() {
                    sendRefundEvent(
                        transactionUUID,
                        "cardReadSuccess",
                        "Card read successfully",
                        null
                    )
                }

                override fun onReadCardFailure(readCardFailure: ReadCardFailure) {
                    sendRefundEvent(
                        transactionUUID,
                        "cardReadFailure",
                        readCardFailure.toString(),
                        null
                    )

                }

                override fun onReaderWaiting() {
                    sendRefundEvent(transactionUUID, "readerWaiting", null, null)
                }

                override fun onReaderReading() {
                    sendRefundEvent(transactionUUID, "readerReading", null, null)
                }

                override fun onReaderRetry() {
                    sendRefundEvent(transactionUUID, "readerRetry", null, null)
                }

                override fun onPinEntering() {
                    sendRefundEvent(transactionUUID, "pinEntering", null, null)
                }

                override fun onReaderFinished() {
                    sendRefundEvent(transactionUUID, "readerFinished", null, null)
                }

                override fun onReaderError(error: String?) {
                    sendRefundEvent(
                        transactionUUID,
                        "readerError",
                        error ?: "Unknown reader error",
                        null
                    )
                }

                override fun onReadingStarted() {
                    sendRefundEvent(transactionUUID, "readingStarted", null, null)
                }
            },
            refundTransactionListener = object : RefundTransactionListener {
                override fun onRefundTransactionSuccess(transactionResponse: io.nearpay.terminalsdk.data.dto.TransactionResponse) {
//                    response(ResponseHandler.success("Refund successful", transactionResponse))
                    val jsonString = gson.toJson(transactionResponse)
                    val map = gson.fromJson(jsonString, Map::class.java) as Map<String, Any>
                    sendRefundEvent(transactionUUID, "sendTransactionSuccessData", null, map)
                }

                override fun onRefundTransactionFailure(refundTransactionFailure: RefundTransactionFailure) {
//                    val errorMessage = refundTransactionFailure.toString()
//                    response(ResponseHandler.error("REFUND_FAILURE", errorMessage))
                    sendRefundEvent(
                        transactionUUID,
                        "sendTransactionFailure",
                        refundTransactionFailure.toString(),
                        null
                    )

                }
            })
    }

    private fun sendRefundEvent(
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
            provider.methodChannel.invokeMethod("refundEvent", eventArgs)
        } catch (e: Exception) {
            // Log the error but do not disrupt the refund flow
            Timber.e(
                e,
                "Failed to send refund event: $eventType for transactionUuid: $transactionUuid"
            )
        }
    }
}