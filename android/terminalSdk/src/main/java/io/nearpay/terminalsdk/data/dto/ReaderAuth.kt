package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReaderAuth (

    @SerialName("auth")
    val auth: Auth,

    @SerialName("reader")
    val reader : ReaderToken
)