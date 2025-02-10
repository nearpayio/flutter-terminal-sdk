package io.nearpay.terminalsdk.data.dto
import kotlinx.serialization.Serializable

@Serializable
data class ReaderEnvelope(
    val card_envelope: CardEnvelope,
    val pin_envelope: PinEnvelope?
)