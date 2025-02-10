package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaselineData(

    @SerialName("device_id")
    var device_id: String,

    @SerialName("sdk")
    var sdk: String,

    @SerialName("base")
    var base: String,

    @SerialName("host")
    var host: String,

    @SerialName("type")
    var type: String,

    @SerialName("device_user")
    var user: String,

    @SerialName("board")
    var board: String,

    @SerialName("model")
    var model: String,

    @SerialName("hardware")
    var hardware: String,

    @SerialName("fingerprint")
    var fingerprint: String,

    @SerialName("incremental")
    var incremental: String,

    @SerialName("versioncode")
    var versioncode: String,

    @SerialName("brand")
    var brand: String,
)

