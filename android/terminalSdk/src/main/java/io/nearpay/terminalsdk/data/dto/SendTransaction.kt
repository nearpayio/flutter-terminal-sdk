package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class SendTransaction(
    val envelope: ReaderEnvelope,
    val location: Location
)