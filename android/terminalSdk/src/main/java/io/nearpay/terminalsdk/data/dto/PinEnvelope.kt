package io.nearpay.terminalsdk.data.dto
import kotlinx.serialization.Serializable

@Serializable
data class PinEnvelope(
    val type: String,
    val key_id: String,
//    val key: EncryptedData?,
    val data_envelope: EncryptedData,
    val key_envelope: EncryptedData?,
)