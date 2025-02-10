//package io.nearpay.terminalsdk.data.dto
//
//import io.nearpay.terminalsdk.data.dto.LocalizationField
//import kotlinx.serialization.SerialName
//import kotlinx.serialization.Serializable
//
//@Serializable
//data class ServerErrorMessage(
//
//    @SerialName("code_text")
//    val code_text: String,
//
//    @SerialName("code")
//    val code: Int,
//
//    @SerialName("error")
//    val error: LocalizationField,
//
//    @SerialName("solution")
//    val solution: LocalizationField,
//
//    @SerialName("level")
//    val level: MessageErrorLevel,
//
//    @SerialName("source")
//    val source: MessageErrorSource
//)
//
//@Serializable
//enum class MessageErrorLevel {
//    @SerialName("HIGH")
//    HIGH,
//    @SerialName("MEDIUM")
//    MEDIUM,
//    @SerialName("LOW")
//    LOW
//}
//
//@Serializable
//data class MessageErrorSource(
//
//    @SerialName("arabic")
//    val arabic: String,
//
//    @SerialName("english")
//    val english: String,
//
//    @SerialName("color")
//    val color: String
//)