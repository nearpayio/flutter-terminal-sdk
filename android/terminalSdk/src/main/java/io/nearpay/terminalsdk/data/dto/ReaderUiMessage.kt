package io.nearpay.terminalsdk.data.dto

data class ReaderUiMessage(
    val messageAr: String,
    val messageEn: String,
    val holdTime: Long = 0
)