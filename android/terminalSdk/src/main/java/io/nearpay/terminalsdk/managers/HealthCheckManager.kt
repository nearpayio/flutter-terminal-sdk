//package io.nearpay.softpos.core.utils.managers
//
//import io.nearpay.softpos.core.BuildConfig
//import io.nearpay.softpos.core.CoreAction
//import io.nearpay.softpos.core.location.LocationManager
//import io.nearpay.softpos.core.utils.AppActionsUtils
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.AppUpdateRequired
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.LocationPermissionMissing
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.LocationUnavailable
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.NfcDisable
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.NoConnectivity
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.PhoneStateUnavailable
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.TerminalReconciling
//import io.nearpay.softpos.core.utils.enums.ActionableHealthCheck.TerminalUpdating
//import io.nearpay.softpos.core.utils.enums.HealthCheck
//import io.nearpay.softpos.core.utils.enums.HealthCheckType.Functionality
//import io.nearpay.softpos.core.utils.enums.HealthCheckType.Security
//import io.nearpay.softpos.core.utils.enums.UnactionableHealthCheck.BaseLineFailed
//import io.nearpay.softpos.core.utils.enums.UnactionableHealthCheck.DeveloperModeOn
//import io.nearpay.softpos.core.utils.enums.UnactionableHealthCheck.NfcNotFound
//import io.nearpay.softpos.core.utils.enums.UnactionableHealthCheck.NotSecure
//import io.nearpay.softpos.core.utils.enums.UnactionableHealthCheck.VpnDetected
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.launch
//
//object HealthCheckManager {
//
//    val job = Job()
//    val scope = CoroutineScope(Dispatchers.IO + job)
//    private var isInvoked = false
//
//    private val healthCheckStatus = mutableMapOf(
//        //Security Health Checks
//        Pair(HealthCheck.Unactionable(NotSecure, Security), false),
//        Pair(HealthCheck.Unactionable(DeveloperModeOn, Security), false),
//        Pair(HealthCheck.Unactionable(VpnDetected, Security), false),
//
//        //Functionality Health Checks
//        Pair(HealthCheck.Actionable(TerminalUpdating, Functionality), false),
//        Pair(HealthCheck.Actionable(TerminalReconciling, Functionality), false),
//        Pair(HealthCheck.Actionable(AppUpdateRequired, Functionality), false),
//        Pair(HealthCheck.Actionable(NoConnectivity, Functionality), false),
//        Pair(HealthCheck.Unactionable(BaseLineFailed, Functionality), false),
//        Pair(HealthCheck.Actionable(NfcDisable, Functionality), false),
//        Pair(HealthCheck.Unactionable(NfcNotFound, Functionality), false),
//        Pair(HealthCheck.Actionable(LocationPermissionMissing, Functionality), false),
//        Pair(HealthCheck.Actionable(LocationUnavailable, Functionality), false),
//    )
//
//    fun getInvalidStatus(): List<HealthCheck> {
//        return healthCheckStatus.filterValues { it }.keys.toList()
//    }
//
//    operator fun invoke(
//        healthCheckUpdate: ((Map<HealthCheck, Boolean>) -> Unit)? = null,
//    ) {
//
//        if (isInvoked) return
//        else isInvoked = true
//
//        scope.launch {
//            SecurityCheckManager.isNotSecureIssue.collectLatest {
//                healthCheckStatus[HealthCheck.Unactionable(NotSecure, Security)] =
//                    if (BuildConfig.DEBUG) false else it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            SecurityCheckManager.isDeveloperModeEnabledIssue.collectLatest {
//                healthCheckStatus[HealthCheck.Unactionable(DeveloperModeOn, Security)] =
//                    if (CoreAction.isHiddenApp && BuildConfig.FLAVOR == "sandbox" || BuildConfig.FLAVOR == "mockNFC" || BuildConfig.DEBUG) false else it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            AppActionsUtils.terminalUpdating.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(TerminalUpdating, Functionality)] = it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            AppActionsUtils.terminalReconciling.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(
//                    TerminalReconciling,
//                    Functionality
//                )] =
//                    it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            AppActionsUtils.appUpdateRequired.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(
//                    AppUpdateRequired,
//                    Functionality
//                )] = it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            NfcManager.nfcDisable.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(NfcDisable, Functionality)] = it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            NfcManager.nfcNotFound.collectLatest {
//                healthCheckStatus[HealthCheck.Unactionable(NfcNotFound, Functionality)] = it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            ConnectionManager.connectivityHasIssue.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(NoConnectivity, Functionality)] =
//                    it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            ConnectionManager.vpnDetected.collectLatest {
//                healthCheckStatus[HealthCheck.Unactionable(VpnDetected, Security)] =
//                    if (CoreAction.isHiddenApp && BuildConfig.FLAVOR == "sandbox" || BuildConfig.DEBUG) false else it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            LocationManager.locationPermissionMissing.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(
//                    LocationPermissionMissing,
//                    Functionality
//                )] = it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            LocationManager.locationUnavailable.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(
//                    LocationUnavailable,
//                    Functionality
//                )] = it
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            PhoneInfoManager.phoneStatePermissionMissing.collectLatest {
//                healthCheckStatus[HealthCheck.Actionable(
//                    PhoneStateUnavailable,
//                    Functionality
//                )] = if (PhoneInfoManager.shouldAskForPhonePermission) it else false
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//
//        scope.launch {
//            BaselineManager.baselineFailed.collectLatest {
//                healthCheckStatus[HealthCheck.Unactionable(BaseLineFailed, Functionality)] =
//                    false
//                healthCheckUpdate?.invoke(healthCheckStatus)
//            }
//        }
//    }
//}