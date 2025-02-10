package io.nearpay.terminalsdk.data.dto

import androidx.annotation.Nullable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginData @JvmOverloads constructor(

    @SerialName("mobile")
    val mobile: String?,

    @SerialName("email")
    val email: String? = null,

    @SerialName("code")
    val code: String,

    @SerialName("device_token")
    val deviceToken: String? = null,


//    @SerialName("publicKey")
//    val publicKey: String
)
