package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(

    @SerialName("lon")
    val long: Double,

    @SerialName("lat")
    val lat: Double,

    @SerialName("is_mock")
    val isLocationFromMock: Boolean? = null,

)