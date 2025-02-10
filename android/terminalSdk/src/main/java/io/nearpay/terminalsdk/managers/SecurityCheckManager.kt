package io.nearpay.softpos.core.utils.managers

import android.content.Context
import android.os.Build
import android.os.UserManager
import android.provider.Settings
import com.guardsquare.dexguard.runtime.detection.CertificateChecker
import com.guardsquare.dexguard.runtime.detection.DebugDetector
import com.guardsquare.dexguard.runtime.detection.EmulatorDetector
import com.guardsquare.dexguard.runtime.detection.HookDetector
import com.guardsquare.dexguard.runtime.detection.RootDetector
import com.guardsquare.dexguard.runtime.detection.VirtualEnvironmentDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.ref.WeakReference

object SecurityCheckManager {

    const val OK = 1

    private val _isNotSecureIssue = MutableStateFlow(false)
    val isNotSecureIssue = _isNotSecureIssue.asStateFlow()

    private val _isDeveloperModeEnabledIssue = MutableStateFlow(false)
    val isDeveloperModeEnabledIssue = _isDeveloperModeEnabledIssue.asStateFlow()

    private val parentJob = SupervisorJob()
    private val coroutineScope = CoroutineScope(parentJob + Dispatchers.IO)

    var onDestroyKeys: () -> Unit = {}

    private val securityCheck = mutableMapOf(
        Pair(SecurityCheck.IS_APK_TAMPERED, false),
        Pair(SecurityCheck.IS_FILE_TAMPERED, false),
        Pair(SecurityCheck.IS_APP_HOOKED, false),
        Pair(SecurityCheck.IS_DEVICE_ROOTED, false),
        Pair(SecurityCheck.IS_EMULATOR_RUNNING, false),
        Pair(SecurityCheck.IS_VIRTUAL_ENV_RUNNING, false),
        Pair(SecurityCheck.IS_CERT_TAMPERED, false),
        Pair(SecurityCheck.IS_DEVICE_DEBUGGABLE, false),
    )

    fun getSecurityCheck() = securityCheck.toMap()

    fun Context.invokeSecurityCheckManager() {

        val weakContext = WeakReference(this)
        val context = weakContext.get()
            ?: return

//        isApkTampered(context)
//        isFileTampered(context)
        isAppHooked(context)
        isDeviceRooted(context)
        isEmulatorRunning(context)
        isVirtualEnvironment(context)
        isCertTampered(context)
        isDeviceDebuggable(context)

        updateFlags()

        coroutineScope.launch {
            while (isActive) {

                val activity = weakContext.get()
                    ?: break

                _isDeveloperModeEnabledIssue.value =
                    isDeveloperModeEnabled(activity) || checkType(
                        SecurityCheck.IS_DEVICE_DEBUGGABLE
                    )
                delay(10 * 1000L) // 10 sec
            }
        }
    }

    private fun checkType(type: SecurityCheck): Boolean {
        return securityCheck.getOrDefault(type, true)
    }

    private fun updateFlags() {
        _isNotSecureIssue.value =
            checkType(SecurityCheck.IS_APK_TAMPERED)
                .or(checkType(SecurityCheck.IS_FILE_TAMPERED))
                .or(checkType(SecurityCheck.IS_APP_HOOKED))
                .or(checkType(SecurityCheck.IS_DEVICE_ROOTED))
                .or(checkType(SecurityCheck.IS_EMULATOR_RUNNING))
                .or(checkType(SecurityCheck.IS_VIRTUAL_ENV_RUNNING))
                .or(checkType(SecurityCheck.IS_CERT_TAMPERED))
    }

//    private fun isApkTampered(context: Context) {
//        val isApkTampered = try {
//            ApkTamperDetector.checkApk(context, OK) != OK
//        } catch (throwable: Throwable) {
//            true
//        }
//        securityCheck[SecurityCheck.IS_APK_TAMPERED] = isApkTampered
//        if (isApkTampered) onDestroyKeys()
//    }

//    private fun isFileTampered(context: Context) {
//        val isFileTampered = try {
//            FileChecker(context).checkAllFiles(OK) != OK
//        } catch (throwable: Throwable) {
//            true
//        }
//        securityCheck[SecurityCheck.IS_FILE_TAMPERED] = isFileTampered
//        if (isFileTampered) onDestroyKeys()
//    }

    private fun isAppHooked(context: Context) {
        val isAppHooked = try {
            HookDetector.isApplicationHooked(context, OK) != OK
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            true
        }
        securityCheck[SecurityCheck.IS_APP_HOOKED] = isAppHooked
        if (isAppHooked) onDestroyKeys()
    }

    private fun isDeviceRooted(context: Context) {
        try {
            RootDetector.checkDeviceRooted(context) { okValue, returnValue ->
                securityCheck[SecurityCheck.IS_DEVICE_ROOTED] = okValue != returnValue
                if (okValue != returnValue) onDestroyKeys()
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            securityCheck[SecurityCheck.IS_DEVICE_ROOTED] = true
            onDestroyKeys()
        }
    }

    private fun isEmulatorRunning(context: Context) {
        val isEmulatorRunning = try {
            EmulatorDetector.isRunningInEmulator(context, OK) != OK
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            true
        }
        securityCheck[SecurityCheck.IS_EMULATOR_RUNNING] = isEmulatorRunning
        if (isEmulatorRunning) onDestroyKeys()
    }

    private fun isVirtualEnvironment(context: Context) {
        val isVirtualEnvironment = try {
            VirtualEnvironmentDetector.isRunningInVirtualEnvironment(context, OK) != OK
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            true
        }

        securityCheck[SecurityCheck.IS_VIRTUAL_ENV_RUNNING] = isVirtualEnvironment
        if (isVirtualEnvironment) onDestroyKeys()
    }

    private fun isCertTampered(context: Context) {

        fun isCertTampered(hash: String): Boolean {
            return CertificateChecker.checkCertificate(context, hash, OK) != OK
        }

        val _isCertTampered = try {
            listOf(
                "BD:49:C9:DD:E6:39:06:AF:1E:28:6F:DE:1C:59:7A:FE:CC:EB:94:63:2D:40:8E:64:C3:88:5B:03:25:46:A8:D6", //stc hash (google-store)
                "92:2E:40:15:D1:BB:60:C4:07:3D:4B:0D:C0:26:B9:6F:C6:EF:ED:61:77:C5:EC:0F:F3:5F:3E:A7:86:FE:22:A8", //digitalPay hash (google-store)
                "70:BB:4F:81:15:89:F5:F7:AD:39:B5:A4:C8:58:8D:57:6E:7E:BD:1A:9D:4C:78:65:B8:06:4D:48:04:EA:5D:71", //digitalPay hash (old-key) (google-store)
                "EB:25:21:31:F0:FF:E0:DC:15:C6:DE:24:50:9E:A7:D5:4C:EB:B2:A8:09:23:D0:7E:9F:7C:A7:FF:9E:C3:18:28", //geidea hash (google-store)
                "B3:87:17:C4:9C:E3:B0:99:64:05:70:D9:18:D5:3D:E1:FE:EC:3D:BB:83:E5:02:D6:11:70:D9:1C:68:A2:1C:75", //sure hash (google-store)
                "85:CA:93:21:EF:03:C5:95:89:E1:09:80:3A:15:1B:48:4B:AB:F9:D8:03:2A:CC:6A:1E:F0:20:EA:9E:B2:4A:76", //cashin hash (google-store)
                "3A:56:84:2E:13:B9:30:1A:A3:F3:15:64:17:CE:98:96:0C:F4:35:AE:D1:7F:79:62:E4:31:F2:DC:AC:5A:C3:1A", //wazen hash (google-store)
                "96:F6:71:62:1B:2B:2B:40:7B:CD:3D:BE:6D:F8:34:C3:C3:CF:F6:1B:E3:E2:A9:E2:BE:98:92:8A:CF:57:0D:DB", //rewaa hash (google-store)
                "89:28:F1:3A:79:43:0B:2E:6E:25:64:1F:42:7A:38:EF:F9:87:21:CF:96:F0:22:2D:36:7A:47:D0:31:6A:A6:F3", //neoleap hash (google-store)
                "05:F3:CC:96:D4:2A:77:2A:72:33:78:79:67:85:A9:14:4C:20:6E:2C:CF:72:C3:C7:89:07:97:89:76:1C:9D:5F", //wosul hash (google-store)
                "B4:0E:78:55:0E:36:20:07:08:C9:A0:89:4F:F6:7D:F9:07:32:A9:E3:8A:D7:0F:D9:89:C9:DE:E9:B9:5B:5F:3B", //payment plugin production hash (google-store production track)
                "A1:51:69:9C:B0:A1:27:BA:0C:35:B0:BE:65:AB:71:27:FA:11:EA:7D:25:B6:4B:09:FD:31:F8:FD:2E:B2:5E:2C", //payment plugin sandbox hash (google-store production track)
                "CC:0A:B6:F3:12:B5:BA:03:1F:A8:FD:1B:BA:FB:7C:20:A4:3B:39:30:B8:3B:EC:81:5E:1C:3C:29:E9:30:B0:C1", //payment plugin production hash (google-store internal testing track)
                "7A:D9:7F:0C:4F:4A:BA:E6:5A:BB:34:44:D1:9B:06:31:CD:D2:C8:A7:35:2C:86:DB:16:BE:E8:8C:D6:AE:1A:C4", //payment plugin sandbox hash (google-store internal testing track)
                "48:DB:90:A0:57:64:8A:A0:7A:05:B7:A3:5A:A3:CA:98:05:46:82:AB:B6:D3:2C:2C:37:75:02:6F:4B:67:84:A8", //releaseHash
                "D5:2A:25:3D:8A:27:CE:ED:E7:0B:DA:50:9C:92:DF:55:93:09:79:B4:FB:EC:3A:D6:90:10:BC:5B:B1:C5:70:82" //debugHash
            ).all { hash ->
                isCertTampered(hash)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            true
        }

        securityCheck[SecurityCheck.IS_CERT_TAMPERED] = _isCertTampered
        if (_isCertTampered) onDestroyKeys()
    }

    private fun isDeviceDebuggable(context: Context) {
        val isDeviceDebuggable = try {
            DebugDetector.isDebuggable(context, OK) != OK
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            true
        }

        securityCheck[SecurityCheck.IS_DEVICE_DEBUGGABLE] = isDeviceDebuggable
        if (isDeviceDebuggable) onDestroyKeys()

    }

    private fun isDeveloperModeEnabled(context: Context): Boolean {
        return try {
            val um = context.getSystemService(Context.USER_SERVICE) as UserManager
            val settingEnabled = Settings.Global.getInt(
                context.contentResolver,
                Settings.Global.DEVELOPMENT_SETTINGS_ENABLED,
                if (Build.TYPE == "eng") 1 else 0
            ) != 0
            val hasRestriction = um.hasUserRestriction(
                UserManager.DISALLOW_DEBUGGING_FEATURES
            )
            val isAdmin = um.isSystemUser
            isAdmin && !hasRestriction && settingEnabled

        } catch (throwable: Throwable) {
            Timber.d(throwable)
            true
        }
    }

    fun finish() {
        this.parentJob.cancelChildren()
    }

    enum class SecurityCheck {
        IS_APK_TAMPERED,
        IS_FILE_TAMPERED,
        IS_APP_HOOKED,
        IS_DEVICE_ROOTED,
        IS_EMULATOR_RUNNING,
        IS_VIRTUAL_ENV_RUNNING,
        IS_CERT_TAMPERED,
        IS_DEVICE_DEBUGGABLE
    }
}