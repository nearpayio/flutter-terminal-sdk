package io.nearpay.terminalsdk.listeners

import io.nearpay.terminalsdk.listeners.failures.ReadCardFailure

interface ReadCardListener {
    fun onReadCardSuccess()
    fun onReadCardFailure(readCardFailure: ReadCardFailure)
    fun onReaderWaiting() // Called when reader is waiting for card
    fun onReaderReading() // Called when reader is reading the card
    fun onReaderRetry() // Called when reader needs to retry
    fun onPinEntering() // Called when PIN is being entered
    fun onReaderFinished() // Called when reading is finished successfully
    fun onReaderError(error: String?) // For any reader errors
    fun onReadingStarted() // Called when reading is started
}