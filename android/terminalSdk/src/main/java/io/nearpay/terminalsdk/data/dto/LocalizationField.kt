package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class LocalizationField(

    @SerialName("arabic")
    val arabic: String = String(),

    @SerialName("english")
    val english: String = String()

): Parcelable
