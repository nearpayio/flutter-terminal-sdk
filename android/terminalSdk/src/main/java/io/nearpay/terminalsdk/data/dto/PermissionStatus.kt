package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermissionStatus(
    @SerialName("permission")
    val permission: String,

    @SerialName("is_granted")
    val isGranted: Boolean
)
