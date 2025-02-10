package com.example.flutter_terminal_sdk.common.operations

import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import com.google.gson.Gson
import io.nearpay.terminalsdk.data.dto.ReconciliationResponse
import io.nearpay.terminalsdk.listeners.GetReconciliationListener
import io.nearpay.terminalsdk.listeners.failures.GetReconciliationFailure
import timber.log.Timber

class GetReconcileDetailsOperation(provider: NearpayProvider) : BaseOperation(provider) {
    private val gson = Gson()
    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {

        val terminalUUID = filter.getString("terminalUUID")
            ?: return response(ResponseHandler.error("MISSING_UUID", "Terminal uuid is required"))

        val uuid = filter.getString("uuid")
            ?: return response(
                ResponseHandler.error(
                    "MISSING_UUID",
                    "Reconciliation uuid is required"
                )
            )

        val terminal =
            provider.activity?.let { provider.terminalSdk?.getTerminal(it, terminalUUID) }

        terminal?.getReconciliation(
            uuid = uuid,
            getReconciliationListener = object : GetReconciliationListener {
                override fun onGetReconciliationSuccess(reconciliationResponse: ReconciliationResponse) {
                    val jsonString = gson.toJson(reconciliationResponse)
                    val map = gson.fromJson(jsonString, Map::class.java) as Map<String, Any>
                    response(ResponseHandler.success("Get Reconciliation Success", map))

//                    sendReconciliationEvent(
//                        uuid,
//                        "getReconciliationDetailsSuccess",
//                        "Reconciliation details fetched successfully",
//                        map
//                    )

                }

                override fun onGetReconciliationFailure(error: GetReconciliationFailure) {
                    Timber.tag("handleReadCard").d("GetReconciliation failed $error")
                    response(ResponseHandler.error("Get Reconciliation Failure", error.toString()))
//
//                    sendReconciliationEvent(
//                        uuid,
//                        "getReconciliationDetailsFailure",
//                        error.toString(),
//                        null
//                    )

                }
            }
        )
    }

    private fun sendReconciliationEvent(
        uuid: String,
        eventType: String,
        message: String?,
        data: Any?
    ) {
        val eventArgs = mutableMapOf<String, Any>(
            "type" to eventType,
            "uuid" to uuid
        )
        message?.let { eventArgs["message"] = it }
        data?.let { eventArgs["data"] = it }

        try {
            provider.methodChannel.invokeMethod("getReconcileDetailEvent", eventArgs)
        } catch (e: Exception) {
            Timber.e(
                e,
                "Failed to send get reconcile details event: $eventType"
            )
        }
    }
}