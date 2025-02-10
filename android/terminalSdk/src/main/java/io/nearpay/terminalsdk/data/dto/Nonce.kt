package io.nearpay.terminalsdk.data.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class Nonce(

    @SerialName("nonce")
    val nonce: String
)
