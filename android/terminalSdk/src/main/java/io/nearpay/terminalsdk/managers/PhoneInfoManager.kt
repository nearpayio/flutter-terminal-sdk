//package io.nearpay.softpos.core.utils.managers
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.os.Build
//import android.telephony.TelephonyManager
//import androidx.annotation.RequiresPermission
//import androidx.core.app.ActivityCompat
//import io.nearpay.terminalsdk.data.dto.SimInfo
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancelChildren
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.ensureActive
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import java.lang.ref.WeakReference
//import java.util.concurrent.atomic.AtomicBoolean
//
//object PhoneInfoManager {
//
//    private var telephonyManager: TelephonyManager? = null
//    private val _PhoneStatePermissionMissing = MutableStateFlow(true)
//    val phoneStatePermissionMissing = _PhoneStatePermissionMissing.asStateFlow()
//
//    private val parentJob = SupervisorJob()
//    private val scope = CoroutineScope(parentJob + Dispatchers.IO)
//
//    var shouldAskForPhonePermission: Boolean = false
//
//    fun Context.invokePhoneStateManager() {
//
//        val weakContext = WeakReference(this)
//
//        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//
//        var isPermissionGranted: AtomicBoolean? = null
//
//        observeLocationPermissionStatus()
//
//        scope.launch {
//            while (isActive) {
//
//                val context = weakContext.get()
//                    ?: break
//
//                val newPermissionValue = checkPermissions(context)
//
//                if (newPermissionValue != isPermissionGranted?.get()) {
//                    isPermissionGranted?.set(newPermissionValue) ?: run {
//                        isPermissionGranted = AtomicBoolean(newPermissionValue)
//                    }
//
//                    onPermissionChanged(newPermissionValue)
//                }
//
//                delay(1000)
//                ensureActive()
//            }
//        }
//
//    }
//
//    private fun onPermissionChanged(isGranted: Boolean) {
//        _PhoneStatePermissionMissing.value = !isGranted
//    }
//
//    private fun checkPermissions(context: Context): Boolean {
//        val phoneStatePermission =
//            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
//        return phoneStatePermission == PackageManager.PERMISSION_GRANTED
//    }
//
//    private fun observeLocationPermissionStatus() {
//        scope.launch {
//            LocationManager.locationPermissionMissing.collectLatest { isLocationPermissionMissing ->
//                if (isLocationPermissionMissing) shouldAskForPhonePermission = true
//            }
//        }
//    }
//
//    fun retrieveSimInfo(): SimInfo? = try {
//            getSimInfo()
//        }  catch (e: SecurityException) {
//            Timber.i(e.message)
//            null
//    }
//
//    @RequiresPermission(value = "android.permission.READ_PHONE_STATE")
//    fun getSimInfo(): SimInfo? {
//
//        if (phoneStatePermissionMissing.value) {
//            return null
//        }
//
//        var cardIdForDefaultEuicc = ""
//        var carrierIdFromSimMccMnc = ""
//        var manufacturerCode = ""
//        var simCarrierIdName = ""
//        var simSpecificCarrierIdName = ""
//        var typeAllocationCode = ""
//        var isDataCapable = ""
//        var isNetworkRoaming = ""
//        var nai = ""
//        var isDataRoamingEnabled = ""
//        var isMultiSimSupported = ""
//        var isDataConnectionAllowed = ""
//        var activeModemCount = ""
//        var networkSelectionMode = ""
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            cardIdForDefaultEuicc = telephonyManager?.cardIdForDefaultEuicc.toString()
//            carrierIdFromSimMccMnc = telephonyManager?.carrierIdFromSimMccMnc.toString()
//            manufacturerCode = telephonyManager?.manufacturerCode.toString()
//            simCarrierIdName = telephonyManager?.simCarrierIdName.toString()
//            simSpecificCarrierIdName = telephonyManager?.simSpecificCarrierIdName.toString()
//            typeAllocationCode = telephonyManager?.typeAllocationCode.toString()
//
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            isDataCapable = telephonyManager?.isDataCapable.toString()
//            isDataConnectionAllowed = telephonyManager?.isDataConnectionAllowed.toString()
//            activeModemCount = telephonyManager?.activeModemCount.toString()
//            isMultiSimSupported = telephonyManager?.isMultiSimSupported.toString()
//            isDataRoamingEnabled = telephonyManager?.isDataRoamingEnabled.toString()
//        }
//
//        return SimInfo(
//            cardIdForDefaultEuicc = cardIdForDefaultEuicc,
//            carrierIdFromSimMccMnc = carrierIdFromSimMccMnc,
//            dataNetworkType = telephonyManager?.dataNetworkType.toString(),
//            deviceSoftwareVersion = telephonyManager?.deviceSoftwareVersion.toString(),
//            manufacturerCode = manufacturerCode,
//            mmsUAProfUrl = telephonyManager?.mmsUAProfUrl.toString(),
//            mmsUserAgent = telephonyManager?.mmsUserAgent.toString(),
//            networkCountryIso = telephonyManager?.networkCountryIso.toString(),
//            networkOperator = telephonyManager?.networkOperator.toString(),
//            networkOperatorName = telephonyManager?.networkOperatorName.toString(),
//            phoneType = telephonyManager?.phoneType.toString(),
//            simCarrierIdName = simCarrierIdName,
//            simCountryIso = telephonyManager?.simCountryIso.toString(),
//            simOperator = telephonyManager?.simOperator.toString(),
//            simOperatorName = telephonyManager?.simOperatorName.toString(),
//            simSpecificCarrierIdName = simSpecificCarrierIdName,
//            simState = telephonyManager?.simState.toString(),
//            networkSpecifier = telephonyManager?.networkSpecifier.toString(),
//            isDataEnabled = telephonyManager?.isDataEnabled.toString(),
//            isNetworkRoaming = telephonyManager?.isNetworkRoaming.toString(),
//            isSmsCapable = telephonyManager?.isSmsCapable.toString(),
//            isWorldPhone = telephonyManager?.isWorldPhone.toString(),
//            hasIccCard = telephonyManager?.hasIccCard().toString(),
//            voiceNetworkType = telephonyManager?.voiceNetworkType.toString(),
//            nai = nai,
//            visualVoicemailPackageName = telephonyManager?.visualVoicemailPackageName.toString(),
//            networkSelectionMode = networkSelectionMode,
//            isDataRoamingEnabled = isDataRoamingEnabled,
//            isMultiSimSupported = isMultiSimSupported,
//            isDataConnectionAllowed = isDataConnectionAllowed,
//            activeModemCount = activeModemCount,
//            isDataCapable = isDataCapable,
//            typeAllocationCode = typeAllocationCode
//        )
//
//
//    }
//
//    fun finish() {
//        parentJob.cancelChildren()
//    }
//}