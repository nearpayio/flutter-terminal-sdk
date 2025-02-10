package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TestEncryptedDataResponse(

    @SerialName("encrypted_data")
    val encryptedData: String,

    @SerialName("iv")
    val iv: String,

    @SerialName("key")
    val key: String?,

    @SerialName("signature")
    val signature: String?
)
