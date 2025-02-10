package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttestationBody(

    @SerialName("device")
    val device: DeviceInfo,

    @SerialName("sim_info")
    val simInfo: SimInfo?,

    @SerialName("checks")
    val checks: CheckInfo,

    @SerialName("nonce_jwt")
    val nonceJwt: String,

    @SerialName("safetynet_jwt")
    val safetynetJwt: String,

    @SerialName("checksum-hash")
    val checkSumHash: String,

    @SerialName("source")
    val source: String,

    @SerialName("error")
    val error: AttestationError? = null
)

@Serializable
data class AttestationError(
    @SerialName("source")
    val source: String,

    @SerialName("message")
    val message: String
)