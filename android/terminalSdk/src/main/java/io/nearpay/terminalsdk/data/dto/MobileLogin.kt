package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MobileLogin(

    @SerialName("mobile")
    val mobile: String
)
