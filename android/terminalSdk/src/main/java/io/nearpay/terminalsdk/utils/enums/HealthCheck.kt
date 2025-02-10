package io.nearpay.softpos.core.utils.enums

sealed class HealthCheck(val id: Int) {
    data class Actionable(val name: ActionableHealthCheck, val type: HealthCheckType): HealthCheck(id = name.id)
    data class Unactionable(val name: UnactionableHealthCheck, val type: HealthCheckType): HealthCheck(id = name.id)
}

enum class ActionableHealthCheck(val id: Int) {
    AppUpdateRequired(3),
    TerminalUpdating(4),
    TerminalReconciling(5),
    NoConnectivity(9),
    NfcDisable(6),
    LocationPermissionMissing(12),
    LocationUnavailable(8),
    PhoneStateUnavailable(15)
}

enum class UnactionableHealthCheck(val id: Int) {
    NotSecure(10),
    DeveloperModeOn(11),
    NfcNotFound(7),
    VpnDetected(13),
    BaseLineFailed(2),
    OperationNotSupported(14)
}

enum class HealthCheckType{
    Security,Functionality
}