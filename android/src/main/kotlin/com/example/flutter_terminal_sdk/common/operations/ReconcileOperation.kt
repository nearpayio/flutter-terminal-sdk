// android/src/main/kotlin/com/example/flutter_terminal_sdk/common/operations/PurchaseOperation.kt

package com.example.flutter_terminal_sdk.common.operations

import com.example.flutter_terminal_sdk.common.NearpayProvider
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import com.example.flutter_terminal_sdk.common.status.ResponseHandler
import com.google.gson.Gson
import io.nearpay.terminalsdk.Terminal
import io.nearpay.terminalsdk.data.dto.ReconciliationReceiptsResponse
import io.nearpay.terminalsdk.listeners.ReconcileListener
import io.nearpay.terminalsdk.listeners.failures.ReconcileFailure
import timber.log.Timber

class ReconcileOperation(provider: NearpayProvider) : BaseOperation(provider) {
    private val gson = Gson()

    override fun run(filter: ArgsFilter, response: (Map<String, Any>) -> Unit) {
        // Extract required arguments
        val terminalUUID = filter.getString("terminalUUID")
            ?: return response(ResponseHandler.error("MISSING_UUID", "Terminal uuid is required"))


        // Retrieve the TerminalSDK instance
        val terminal: Terminal = provider.terminalSdk?.getTerminal(provider.activity!!, terminalUUID)
            ?: return response(
                ResponseHandler.error(
                    "TERMINAL_NOT_FOUND",
                    "Terminal with uuid = $terminalUUID = not found"
                )
            )
        Timber.d("Got Terminal successfully")
        try {
            // Initiate the purchase process
            terminal.reconcile(reconcileListener = object : ReconcileListener {
                override fun onReconcileFailure(error: ReconcileFailure) {
                    Timber.tag("onReconcileFailure").d("Reconcile failed $error")
                    response(ResponseHandler.error("Reconcile Failure", error.toString()))
                }

                override fun onReconcileSuccess(reconciliationReceiptsResponse: ReconciliationReceiptsResponse) {
                    val jsonString = gson.toJson(reconciliationReceiptsResponse)
                    val map = gson.fromJson(jsonString, Map::class.java) as Map<String, Any>
                    response(ResponseHandler.success("Reconcile Success", map))
                }
            })
        } catch (e: Exception) {
            // Handle any unexpected exceptions during purchase
            response(ResponseHandler.error("RECONCILE_FAILED", "Reconcile failed: ${e.message}"))
        }
    }

}
