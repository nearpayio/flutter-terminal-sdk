package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheckInfo(
    @SerialName("is_apk_tampered")
    val isApkTampered: Boolean,

    @SerialName("is_cert_tampered")
    val isCertTampered: Boolean,

    @SerialName("is_hooked")
    val isHooked: Boolean,

    @SerialName("is_debuggable")
    val isDebuggable: Boolean,

    @SerialName("is_rooted")
    val isRooted: Boolean,

    @SerialName("is_emulator")
    val isEmulator: Boolean,

    @SerialName("is_virtual")
    val isVirtual: Boolean,

    @SerialName("is_malware_detected")
    val isMalwareDetected: Boolean,
)
