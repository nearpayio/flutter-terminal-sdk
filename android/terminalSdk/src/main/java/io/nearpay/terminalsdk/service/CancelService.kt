package io.nearpay.softpos.core.utils.service

import android.app.Service
import android.content.Intent
import android.os.*

class CancelService : Service() {

    private var sdkMessenger: Messenger? = null

    companion object {
        var onCancelRequest: (() -> Unit)? = null
        var onCancelWithReverseRequest: ((String) -> Unit)? = null
    }

    private val incomingHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msgFromSdk: Message) {
            super.handleMessage(msgFromSdk)
            sdkMessenger = msgFromSdk.replyTo
            val receivedBundle = msgFromSdk.data

            val performCancel = receivedBundle.getBoolean("cancelTransaction")
            if (performCancel) {
                val status = onCancelRequest != null
                sendReply("cancelStatus", status)
                onCancelRequest?.invoke()
            }

            val performCancelWithReverse = receivedBundle.getBoolean("cancelTransactionWithReverse")
            val requestId = receivedBundle.getString("requestId").orEmpty()
            if (performCancelWithReverse) {
                val status = onCancelWithReverseRequest != null
                sendReply("cancelWithReverseStatus", status)
                onCancelWithReverseRequest?.invoke(requestId)
            }

         }
    }

    fun sendReply(key: String, status: Boolean) {
        val paymentPluginMessage = Message.obtain(null, 0)
        val replyBundle = Bundle()
        replyBundle.putBoolean(key, status)
        paymentPluginMessage.data = replyBundle
        sdkMessenger?.send(paymentPluginMessage)
    }

    override fun onBind(intent: Intent?): IBinder {
        return when (intent?.action) {
            "pluginComponent" -> ServiceBinder()
            "pluginService" -> Messenger(incomingHandler).binder
            else -> Messenger(incomingHandler).binder
        }
    }

    inner class ServiceBinder : Binder() {
        val service: CancelService
            get() = this@CancelService
    }

}