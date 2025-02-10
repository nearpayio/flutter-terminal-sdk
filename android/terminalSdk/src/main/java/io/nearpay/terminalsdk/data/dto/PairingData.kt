package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PairingData(
    @SerialName("pairing_code") var code: String? = null,
    @SerialName("token") var token: String? = null,
    @SerialName("room_id") var roomId: String? = null
)
