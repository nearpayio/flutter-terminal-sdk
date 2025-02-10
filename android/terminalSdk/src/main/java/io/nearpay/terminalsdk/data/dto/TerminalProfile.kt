package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class NullableTerminalProfile(
    @SerialName("merchant") val merchant: TerminalProfileMerchant? = null,

    @SerialName("admin_pin_hash") val admin_pin_hash: String? = null,

    @SerialName("terminal") val terminal: TerminalProfileDetails? = null,

    @SerialName("hash") val terminal_hash: String? = null,

    @SerialName("supported_operations")
    val supportedOperations: SupportedOperations? = null,

    @SerialName("vas_merchant")
    val vasMerchant: VasMerchant? = null,

    @SerialName("branding")
    val branding: Branding = Branding(),
)

@Serializable
data class TerminalProfile(

    @SerialName("merchant") val merchant: TerminalProfileMerchant,

    @SerialName("admin_pin_hash") val admin_pin_hash: String,

    @SerialName("terminal") val terminal: TerminalProfileDetails,

    @SerialName("generated_terminal_id") var generatedTerminalID: String? = null,

    @SerialName("hash") val terminal_hash: String,

    @SerialName("supported_operations")
    val supportedOperations: SupportedOperations? = null,

    @SerialName("vas_merchant")
    val vasMerchant: VasMerchant? = null,

    @SerialName("branding")
    val branding: Branding? = null,
)

@Serializable
data class Branding(

    @SerialName("welcome_screen")
    val welcomeScreen: WelcomeScreen? = null,

    @SerialName("logo") val logo: String? = null,

    @SerialName("logo_layout") val logoLayout: String? = null,
)


@Serializable
data class WelcomeScreen(

    @SerialName("id")
    val id: String? = null,

    @SerialName("frames")
    val frames: List<String>? = null,

    @SerialName("interval")
    val interval: Long? = 5000, // in ms

    @SerialName("created_at")
    val createdAt: String? = null,

    @SerialName("updated_at")
    val updatedAt: String? = null
)

@Serializable
data class TerminalProfileMerchant(

    @SerialName("name") val name: LocalizationField,

    @SerialName("address") val address: LocalizationField,
)

@Serializable
data class TerminalProfileDetails(

    @SerialName("tid") val tid: String,

    @SerialName("uuid") val uuid: String,

    @SerialName("display_messages") val display_messages: DisplayMessages,

    @SerialName("emv") val emv: JsonObject,

    @SerialName("bins") val bins: ArrayList<Bin>? = ArrayList()
)

@Serializable
data class DisplayMessages(

    @SerialName("enter_pin") val enter_pin: LocalizationField,

    @SerialName("approved") val approved: LocalizationField,

    @SerialName("remove_card") val remove_card: LocalizationField,

    @SerialName("present_card_or_device") val present_card_or_device: LocalizationField,

    @SerialName("please_wait") val please_wait: LocalizationField,

    @SerialName("insert_card") val insert_card: LocalizationField,

    @SerialName("try_again") val try_again: LocalizationField,

    @SerialName("processing_error") val processing_error: LocalizationField
)

@Serializable
data class Bin(

    @SerialName("start") val start: String,

    @SerialName("end") val end: String,

    @SerialName("card_scheme_id") val card_scheme_id: String,

    @SerialName("card_scheme_sponsor_id") val card_scheme_sponsor_id: String
)

fun TerminalProfile?.loadTerminalProfile(
//    nearPaySharedPreferences: NearPaySharedPreferences
): Boolean {
    if (this == null)
        return false

//    val isSaved = nearPaySharedPreferences.saveTerminalProfile(this)


//    if (!isSaved) {
//        return false // TODO fix me later
//    }

    // TODO call equivalent of SpinActions.updateConfiguration()
    // TODO fix me later
    return true
}

fun NullableTerminalProfile.getTerminalProfileOrNull(): TerminalProfile? {
    if (terminal == null)
        return null

    if (terminal_hash == null)
        return null

    if (merchant == null)
        return null

    if (admin_pin_hash == null)
        return null

    return TerminalProfile(
        terminal = terminal,
        terminal_hash = terminal_hash,
        merchant = merchant,
        admin_pin_hash = admin_pin_hash,
        supportedOperations = supportedOperations,
        vasMerchant = vasMerchant,
        branding = Branding(welcomeScreen = branding.welcomeScreen,
                            logo = branding.logo,
                            logoLayout = branding.logoLayout)
    )
}

@Serializable
data class SupportedOperations(
    @SerialName("purchase")
    val purchase: Boolean? = null,

    @SerialName("refund")
    val refund: Boolean? = null,

    @SerialName("reversal")
    val reversal: Boolean? = null,

    @SerialName("authorization")
    val authorization: Boolean? = null,

    @SerialName("reconciliations")
    val reconciliations: Boolean? = null,

    @SerialName("transactions_reports")
    val transactionsReports: Boolean? = null,

    @SerialName("reconciliations_reports")
    val reconciliationsReports: Boolean? = null,

    @SerialName("purchase_with_invoice_number")
    val purchase_with_invoice_number: Boolean? = null,
)

@Serializable
data class VasMerchant(
    @SerialName("merchant_hash")
    val merchant_hash: String? = null,

    @SerialName("merchant_url")
    val merchant_url: String? = null,

    @SerialName("merchant_id")
    val merchant_id: String? = null
)