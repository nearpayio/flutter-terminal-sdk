package io.nearpay.terminalsdk.utils

import android.os.Handler
import android.os.Looper


fun runOnMainThread(block: () -> Unit) {
    Handler(Looper.getMainLooper()).post {
        block()
    }
}