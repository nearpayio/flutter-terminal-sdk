package io.nearpay.softpos.core.utils.service

import android.app.Service
import android.content.Intent
import android.os.*

class DismissService : Service() {

    private var sdkMessenger: Messenger? = null

    companion object {
        var onDismissRequest: (() -> Unit)? = null
    }

    private val incomingHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msgFromSdk: Message) {
            super.handleMessage(msgFromSdk)
            sdkMessenger = msgFromSdk.replyTo
            val receivedBundle = msgFromSdk.data

            val dismissStatus = receivedBundle.getBoolean("dismissStatus")
            if (dismissStatus) {
                val status = onDismissRequest != null
                sendReply("dismissStatus", status)
                onDismissRequest?.invoke()
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
            "dismissService" -> Messenger(incomingHandler).binder
            else -> Messenger(incomingHandler).binder
        }
    }

}