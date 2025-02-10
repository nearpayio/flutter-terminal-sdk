package io.nearpay.terminalsdk.data.dto

import io.nearpay.softpos.core.utils.enums.TerminalMode
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TerminalsResponse(
    @SerialName("data")
    val terminalsResponse: List<TerminalResponse>,

    @SerialName("pagination")
    val pagination: Pagination,
)



@Serializable
data class TerminalMerchant(

    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: LocalizationField,

    @SerialName("created_at")
    val createdAt: String? = ""
)

@Serializable
data class TerminalResponse(
    @SerialName("name")
    val name: String? = null,

    @SerialName("tid")
    val tid: String,

    @SerialName("uuid")
    val uuid: String,

    @SerialName("os_type")
    val osType: String,

    @SerialName("busy")
    val busy: Boolean = false,

    @SerialName("mode")
    val mode: String = TerminalMode.POS.id,

    @SerialName("is_locked")
    val isLocked: Boolean = false,

    @SerialName("has_profile")
    val hasProfile: Boolean = false,

    @SerialName("merchant")
    val merchant: TerminalMerchant

)

sealed class TerminalListItem {
    data class Item(val terminalResponse: TerminalResponse) : TerminalListItem()
    data class Header(val name: LocalizationField) : TerminalListItem()
}