package io.nearpay.terminalsdk.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimInfo(
    @SerialName("cardIdForDefaultEuicc") val cardIdForDefaultEuicc: String,
    @SerialName("carrierIdFromSimMccMnc") val carrierIdFromSimMccMnc: String,
    @SerialName("dataNetworkType") val dataNetworkType: String,
    @SerialName("deviceSoftwareVersion") val deviceSoftwareVersion: String,
    @SerialName("manufacturerCode") val manufacturerCode: String,
    @SerialName("mmsUAProfUrl") val mmsUAProfUrl: String,
    @SerialName("mmsUserAgent") val mmsUserAgent: String,
    @SerialName("networkCountryIso") val networkCountryIso: String,
    @SerialName("networkOperator") val networkOperator: String,
    @SerialName("networkOperatorName") val networkOperatorName: String,
    @SerialName("phoneType") val phoneType: String,
    @SerialName("simCarrierIdName") val simCarrierIdName: String,
    @SerialName("simCountryIso") val simCountryIso: String,
    @SerialName("simOperator") val simOperator: String,
    @SerialName("simOperatorName") val simOperatorName: String,
    @SerialName("simSpecificCarrierIdName") val simSpecificCarrierIdName: String,
    @SerialName("simState") val simState: String,
    @SerialName("nai") val nai: String,
    @SerialName("visualVoicemailPackageName") val visualVoicemailPackageName: String,
    @SerialName("voiceNetworkType") val voiceNetworkType: String,
    @SerialName("networkSelectionMode") val networkSelectionMode: String,
    @SerialName("hasIccCard") val hasIccCard: String,
    @SerialName("isWorldPhone") val isWorldPhone: String,
    @SerialName("isSmsCapable") val isSmsCapable: String,
    @SerialName("isNetworkRoaming") val isNetworkRoaming: String,
    @SerialName("isDataRoamingEnabled") val isDataRoamingEnabled: String,
    @SerialName("isMultiSimSupported") val isMultiSimSupported: String,
    @SerialName("isDataEnabled") val isDataEnabled: String,
    @SerialName("isDataConnectionAllowed") val isDataConnectionAllowed: String,
    @SerialName("activeModemCount") val activeModemCount: String,
    @SerialName("networkSpecifier") val networkSpecifier: String,
    @SerialName("isDataCapable") val isDataCapable: String,
    @SerialName("typeAllocationCode") val typeAllocationCode: String
    )

