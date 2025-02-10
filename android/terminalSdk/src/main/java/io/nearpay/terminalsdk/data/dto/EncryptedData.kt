package io.nearpay.terminalsdk.data.dto
import kotlinx.serialization.Serializable

@Serializable
data class EncryptedData(

    val encrypted: String,

    val iv: String,

    val auth_tag: String?,
)