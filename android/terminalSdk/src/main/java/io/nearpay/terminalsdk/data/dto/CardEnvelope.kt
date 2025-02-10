package io.nearpay.terminalsdk.data.dto
import kotlinx.serialization.Serializable

@Serializable
data class CardEnvelope(
    val type: String,
    val key_id: String,

    val data_envelope: EncryptedData,
    val key_envelope: EncryptedData?,
)