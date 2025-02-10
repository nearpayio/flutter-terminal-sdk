package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class LabelField<T: Any>(

    @SerialName("label")
    val label: LocalizationField,

    @SerialName("value")
    val value: @RawValue T

): Parcelable