package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestConnect(

    @SerialName("uuid") var uuid: String? = null,
    @SerialName("code") var code: String? = null,
    @SerialName("client_id") var clientId: String? = null,
    @SerialName("email") var email: String? = null,
    @SerialName("model") var model: String? = null,
    @SerialName("app") var app: String? = null,
    @SerialName("timeout") var timeout: Long? = null
)
