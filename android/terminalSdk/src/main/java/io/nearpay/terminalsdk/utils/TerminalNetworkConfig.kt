package io.nearpay.terminalsdk.utils

import io.nearpay.terminalsdk.R

private const val pattern = "*.nearpay.io"
private const val mtlsSandboxPins1 = "sha256/0F8cQ4D7q7x8V+ktKz/pd9UceewHKjbewjeYyMqaKsk="
private const val mtlsSandboxPins2 = "sha256/hkhJkf/S424WWe7tiaSNnGlps9yOycOBN9R4jz5CAtk="
private const val mtlsProductionPins1 = "sha256/vLosaeDuS98g4M+INOFd75IXAqUL5mgxt1SyF27BK4g="
private const val mtlsProductionPins2 = "sha256/dE4dDKopu8ha93Q4fJJ/hYJaQYmVCcBUMx6p4dVoXzw="

enum class NetworkClientConfig(
    val url: String,
    val ip: ByteArray?,
    val pins: List<Pin>?,
    var serverCertificate: Int?,
    var clientKeystore: Int?,
    var clientKeystorePassword: String?
) {

    SANDBOX_MTLS_1(
        url = "https://sa-sandbox-terminal-sdk.nearpay.io/",
        ip = byteArrayOf(158.toByte(), 101.toByte(), 242.toByte(), 225.toByte()),
        pins = listOf(Pin(pattern, mtlsSandboxPins1), Pin(pattern, mtlsSandboxPins2)),
        serverCertificate = R.raw.terminal_sdk_crt,
        clientKeystore = R.raw.terminal_sdk,//R.raw.client_sandbox,
        clientKeystorePassword = "sCBqKaTX)@%EnV1=}n-Ky10[D0J)cfU[",//"pEi%Hb}SOdyl;Il7E';=GA4E5qcp%7C6"
    ),
    SANDBOX_MTLS_2(
        url = "https://sa-sandbox-terminal-sdk.nearpay.io/",
        ip = byteArrayOf(34.toByte(), 166.toByte(), 41.toByte(), 131.toByte()),
        pins = listOf(Pin(pattern, mtlsSandboxPins1), Pin(pattern, mtlsSandboxPins2)),
        serverCertificate = R.raw.terminal_sdk_crt,
        clientKeystore = R.raw.terminal_sdk,//R.raw.client_sandbox,
        clientKeystorePassword = "sCBqKaTX)@%EnV1=}n-Ky10[D0J)cfU[",//"pEi%Hb}SOdyl;Il7E';=GA4E5qcp%7C6"
    ),
    PRODUCTION_MTLS_1(
        url = "https://sa-reader.nearpay.io/reader/v2/devices/",
        ip = byteArrayOf(144.toByte(), 24.toByte(), 209.toByte(), 87.toByte()),
        pins = listOf(Pin(pattern, mtlsProductionPins1), Pin(pattern, mtlsProductionPins2)),
        serverCertificate = R.raw.client_production,
        clientKeystore = null,//R.raw.client_production,
        clientKeystorePassword = null,//"x0&lE)aj}tU^4B(Tb{kgK0E^6}'e+.MM"
    ),
    PRODUCTION_MTLS_2(
        url = "https://sa-reader.nearpay.io/reader/v2/devices/",
        ip = byteArrayOf(34.toByte(), 166.toByte(), 75.toByte(), 228.toByte()),
        pins = listOf(Pin(pattern, mtlsProductionPins1), Pin(pattern, mtlsProductionPins2)),
        serverCertificate = R.raw.client_production,
        clientKeystore = null,//R.raw.client_production,
        clientKeystorePassword = null//"x0&lE)aj}tU^4B(Tb{kgK0E^6}'e+.MM"
    ),
    TR_SANDBOX_MTLS_1(
        url = "https://aldie-reader.nearvault.com/v2/devices/",
        ip = null,
        pins = null,
        serverCertificate = null,
        clientKeystore = null,
        clientKeystorePassword = null
    ),
    TR_PRODUCTION_MTLS_1(
        url = "https://tr-reader.nearpay.io/trust/v1/devices/",
        ip = byteArrayOf(34.toByte(), 166.toByte(), 75.toByte(), 228.toByte()),
        pins = listOf(Pin(pattern, mtlsProductionPins1), Pin(pattern, mtlsProductionPins2)),
        serverCertificate = null,
        clientKeystore = null,
        clientKeystorePassword = null
    );

}

data class Pin(val pattern: String, val pins: String)
