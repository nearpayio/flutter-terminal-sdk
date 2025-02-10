//package io.nearpay.terminalsdk.data.remote
//
//import io.nearpay.softpos.R
//
//private const val pattern = "*.nearpay.io"
//private const val mtlsSandboxPins1 = "sha256/0F8cQ4D7q7x8V+ktKz/pd9UceewHKjbewjeYyMqaKsk="
//private const val mtlsSandboxPins2 = "sha256/hqk7C3cvop6ndCdtYr+pSDvMLzpwKkgOXuGwPJeJSxM="
//private const val mtlsProductionPins1 = "sha256/vLosaeDuS98g4M+INOFd75IXAqUL5mgxt1SyF27BK4g="
//private const val mtlsProductionPins2 = "sha256/dE4dDKopu8ha93Q4fJJ/hYJaQYmVCcBUMx6p4dVoXzw="
//
//const val REFRESH_ENDPOINT = "refresh"
//const val cert = "-----BEGIN CERTIFICATE-----\n" +
//        "MIIDuzCCAqOgAwIBAgIUQeroRrBPq+0qqqY200D/UHlaf70wDQYJKoZIhvcNAQEL\n" +
//        "BQAwgYUxCzAJBgNVBAYTAlNBMQ8wDQYDVQQIDAZSaXlhZGgxDzANBgNVBAcMBlJp\n" +
//        "eWFkaDEQMA4GA1UECgwHTmVhcnBheTENMAsGA1UECwwEcm9vdDEVMBMGA1UEAwwM\n" +
//        "Ki5uZWFycGF5LmlvMRwwGgYJKoZIhvcNAQkBFg1pdEBuZWFycGF5LmlvMB4XDTI0\n" +
//        "MDkyOTEwNDMzMVoXDTM0MDkyNzEwNDMzMVowgYUxCzAJBgNVBAYTAlNBMQ8wDQYD\n" +
//        "VQQIDAZSaXlhZGgxDzANBgNVBAcMBlJpeWFkaDEQMA4GA1UECgwHTmVhcnBheTEN\n" +
//        "MAsGA1UECwwEcm9vdDEVMBMGA1UEAwwMKi5uZWFycGF5LmlvMRwwGgYJKoZIhvcN\n" +
//        "AQkBFg1pdEBuZWFycGF5LmlvMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC\n" +
//        "AQEA79r5x0Y1gS+0o1/S0vkNgi9/Lnu/oSC0rAGWjZmmu5tfynBU5vyOjRkqPnFZ\n" +
//        "QnV6Nby40Y0a/rn0Q+i8fq5ICyMkb7wmHm0fVXzu4cse+1AuF7oB+iKdoNuRt3Ad\n" +
//        "EQztpCFI6FT/CnvGgyHwogOEbrBLezvP8qsAYn9iHwFue9Sl6a+CniKVH5Le+yuo\n" +
//        "9z0mVFiCSYgZCCsnSejUrCjKQGY4n0J+DQkVRRft7dgsax6gA8f2B2Io/XNmileO\n" +
//        "ZAgTh63wZsmng4zT2FzkFfrLrz2QRREVpAvE0fhZVgQUsLxa3TbAFa4FxmQhh+uj\n" +
//        "VcfVo2q0wW8gZ3qjpYa7gGMQPwIDAQABoyEwHzAdBgNVHQ4EFgQUZMzl9hGzChJq\n" +
//        "P7S2sWoZ7T3mgoswDQYJKoZIhvcNAQELBQADggEBAJZvROg7iNR5PVlexeAwP3zK\n" +
//        "DTnbEXWVrXL+dsPh4xr7JWMcDRp+4T+CLI4xXeZj4n8/IkpxnoYC6p77ECvZTo37\n" +
//        "SE+Kz3jKaLjiApvDtIYdS41fuohyaFsgatDTWG2ZRHMmO2Na+ne9hTwvWujtj3Gy\n" +
//        "mikwQw0ggqpr1aY01qbSGcE2VqAntvadyq01Il3Jqb1ClVYKvu0zWD9GyY1i2rlP\n" +
//        "l92U/hC7KGsIV+C9he7WS2Sj9DQTf1fHbGZ5Is/n77hbJaOqEf9BHPJ+TJEG5ZlW\n" +
//        "n+EoaV8pt9mU8KXjfEbix3V0ZGjNf1E+ig0CDgaVYxV/QCtfQz/L+3s2pD1mwR0=\n" +
//        "-----END CERTIFICATE-----\n"
//const val NEW_PROD_CERT = "-----BEGIN CERTIFICATE-----\n" +
//        "MIIDuzCCAqOgAwIBAgIUaGgohkGkunSuLUGssVeefNjIGY4wDQYJKoZIhvcNAQEL\n" +
//        "BQAwgYUxCzAJBgNVBAYTAlNBMQ8wDQYDVQQIDAZSaXlhZGgxDzANBgNVBAcMBlJp\n" +
//        "eWFkaDEQMA4GA1UECgwHTmVhcnBheTENMAsGA1UECwwEUm9vdDEVMBMGA1UEAwwM\n" +
//        "Ki5uZWFycGF5LmlvMRwwGgYJKoZIhvcNAQkBFg1pdEBuZWFycGF5LmlvMB4XDTI0\n" +
//        "MDkzMDA0MjAwNFoXDTM0MDkyODA0MjAwNFowgYUxCzAJBgNVBAYTAlNBMQ8wDQYD\n" +
//        "VQQIDAZSaXlhZGgxDzANBgNVBAcMBlJpeWFkaDEQMA4GA1UECgwHTmVhcnBheTEN\n" +
//        "MAsGA1UECwwEUm9vdDEVMBMGA1UEAwwMKi5uZWFycGF5LmlvMRwwGgYJKoZIhvcN\n" +
//        "AQkBFg1pdEBuZWFycGF5LmlvMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC\n" +
//        "AQEAuyPHVN/UYOBhmy17GFpjmPrlqiq2AZBRA8ogxsFEkdrYnp/+g4mxWS+AazaT\n" +
//        "lL072yEgZ8+jNdd4VX+OYbTm8SPFsvRUcHXGONNPtazFAfR/+5wjFRlg3CVXB6XO\n" +
//        "C7zHT1HnyyXvvd7kv0dBfzXjmar5+lfyZXvU7rTXEmVaeRguzd2c2zhI9ukJn1fS\n" +
//        "z2dDXVH2XboKBwDuLQ+ytuzpDHgqldNYr3hQ3PQ1pkZ0hxfxjzqMaxFZmMwpoE06\n" +
//        "Mi8X33cTGCGqWr1TKHUtHVZV9pHRKBUpV11ZvsUmiF0VdH18IGIwhotlfRAaeSnG\n" +
//        "qM624yd4SLvawxoZsWhq+TbycwIDAQABoyEwHzAdBgNVHQ4EFgQUIO/gtskuWMOW\n" +
//        "OK+CWNvao4JKWkQwDQYJKoZIhvcNAQELBQADggEBAD8XWR021fQzWYPUgiN9zwUC\n" +
//        "mJY2dLT52UjrnMgOTXl0gFm6bRSM2MZs2yvIiFjVexJGYymmzfOnNnabABaqwvjl\n" +
//        "jBK/C5vu6iQ/MEW5pCkOmppJ0VcfJnPHELYlnZk6MvZ9zz9h0ddXMoAOWznhJ9Tq\n" +
//        "YCj63zoEXnxLWUz00UKpU5SpBNjNDLEWolAQEI7lxEq4isC4/8qeXTsFGfXGvWLS\n" +
//        "iBSfgQOoUvCIviPvvWsEHgVJUf7gDGDCPbmd2qW97XLrkBoXWJ3mdPLodsww/WuT\n" +
//        "2HwkVzTAxlCpxpsnug+z0Zw6cLKd92XmFw2VppFoXigIrNEzknFQpJ0va+WXx1c=\n" +
//        "-----END CERTIFICATE-----\n"
//
//enum class TerminalNetworkConfig(
//    val url: String,
//    val ip: ByteArray?,
//    val pins: List<Pin>?,
//    var serverCertificate: Int?,
//    var clientKeystore: Int?,
//    var clientKeystorePassword: String?
//) {
//
//    SANDBOX_MTLS_1(
//        url = "https://sa-sandbox-reader.nearpay.io/reader/v2/devices/",
//        ip = byteArrayOf(158.toByte(), 101.toByte(), 242.toByte(), 225.toByte()),
//        pins = listOf(Pin(pattern, mtlsSandboxPins1), Pin(pattern, mtlsSandboxPins2)),
//        serverCertificate = R.raw.cert,
//        clientKeystore = null,//R.raw.client_sandbox,
//        clientKeystorePassword = null,//"pEi%Hb}SOdyl;Il7E';=GA4E5qcp%7C6"
//    ),
//    SANDBOX_MTLS_2(
//        url = "https://sa-sandbox-reader.nearpay.io/reader/v2/devices/",
//        ip = byteArrayOf(34.toByte(), 166.toByte(), 41.toByte(), 131.toByte()),
//        pins = listOf(Pin(pattern, mtlsSandboxPins1), Pin(pattern, mtlsSandboxPins2)),
//        serverCertificate = R.raw.cert,
//        clientKeystore = null,//R.raw.client_sandbox,
//        clientKeystorePassword = null,//"pEi%Hb}SOdyl;Il7E';=GA4E5qcp%7C6"
//    ),
//    PRODUCTION_MTLS_1(
//        url = "https://sa-reader.nearpay.io/reader/v2/devices/",
//        ip = byteArrayOf(144.toByte(), 24.toByte(), 209.toByte(), 87.toByte()),
//        pins = listOf(Pin(pattern, mtlsProductionPins1), Pin(pattern, mtlsProductionPins2)),
//        serverCertificate = R.raw.prod_cert,
//        clientKeystore = null,//R.raw.client_production,
//        clientKeystorePassword = null,//"x0&lE)aj}tU^4B(Tb{kgK0E^6}'e+.MM"
//    ),
//    PRODUCTION_MTLS_2(
//        url = "https://sa-reader.nearpay.io/reader/v2/devices/",
//        ip = byteArrayOf(34.toByte(), 166.toByte(), 75.toByte(), 228.toByte()),
//        pins = listOf(Pin(pattern, mtlsProductionPins1), Pin(pattern, mtlsProductionPins2)),
//        serverCertificate = R.raw.prod_cert,
//        clientKeystore = null,//R.raw.client_production,
//        clientKeystorePassword = null//"x0&lE)aj}tU^4B(Tb{kgK0E^6}'e+.MM"
//    ),
//    TR_SANDBOX_MTLS_1(
//        url = "https://aldie-reader.nearvault.com/v2/devices/",
//        ip = null,
//        pins = null,
//        serverCertificate = null,
//        clientKeystore = null,
//        clientKeystorePassword = null
//    ),
//    TR_PRODUCTION_MTLS_1(
//        url = "https://tr-reader.nearpay.io/trust/v1/devices/",
//        ip = byteArrayOf(34.toByte(), 166.toByte(), 75.toByte(), 228.toByte()),
//        pins = listOf(Pin(pattern, mtlsProductionPins1), Pin(pattern, mtlsProductionPins2)),
//        serverCertificate = R.raw.prod_cert,
//        clientKeystore = null,
//        clientKeystorePassword = null
//    );
//
//    fun configureCertificates(
//        clientKey: Int,
//        clientKeyPassword: String
//    ): TerminalNetworkConfig {
//        return this.apply {
//            clientKeystore = clientKey
//            clientKeystorePassword = clientKeyPassword
//        }
//    }
//}
//
//data class Pin(val pattern: String, val pins: String)
