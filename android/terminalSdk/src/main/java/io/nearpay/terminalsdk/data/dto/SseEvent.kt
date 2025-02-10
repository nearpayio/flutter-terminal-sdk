package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SseEvent(

    @SerialName("payload") var payload: Payload? = Payload(),
    @SerialName("request") var request: Request? = Request()

)
@Serializable
data class Payload(
    @SerialName("jwt" ) var jwt : String? = null,
)

@Serializable
data class Request(

    @SerialName("user") var user: String? = null,
    @SerialName("requestId") var requestId: String? = null

)