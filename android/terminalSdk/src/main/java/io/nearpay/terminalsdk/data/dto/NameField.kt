package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class NameField<T: Any>(

    @SerialName("name")
    val name: LocalizationField,

    @SerialName("id")
    val id: @RawValue T

): Parcelable
