//package io.nearpay.softpos.core.domain.attestationRequestUseCases
//
//import android.content.Context
//import android.os.Build
//import io.nearpay.terminalsdk.data.dto.CheckInfo
//import io.nearpay.terminalsdk.data.dto.DeviceInfo
//import io.nearpay.softpos.core.utils.managers.SecurityCheckManager
//import io.nearpay.terminalsdk.data.local.NearPaySharedPreferences
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import java.io.File
//import java.io.FileInputStream
//import java.security.MessageDigest
//
//fun getDeviceInfo() =
//    DeviceInfo(
//        model = Build.MODEL,
//        device_id = Build.ID,
//        brand = Build.BRAND,
//        type = Build.TYPE,
//        deviceUser = Build.USER,
//        base = Build.VERSION_CODES.BASE.toString(),
//        incremental = Build.VERSION.INCREMENTAL,
//        sdk = Build.VERSION.SDK_INT.toString(),
//        board = Build.BOARD,
//        host = Build.HOST,
//        fingerprint = Build.FINGERPRINT,
//        versioncode = Build.VERSION.RELEASE,
//        hardware = Build.HARDWARE
//    )
//
//fun getSecurityCheck(type: SecurityCheckManager.SecurityCheck): Boolean {
//    return SecurityCheckManager.getSecurityCheck().getOrDefault(type, true)
//}
//
//fun getCheckInfo() =
//    CheckInfo(
//        isApkTampered = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_APK_TAMPERED),
//        isCertTampered = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_CERT_TAMPERED),
//        isHooked = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_APP_HOOKED),
//        isDebuggable = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_DEVICE_DEBUGGABLE),
//        isRooted = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_DEVICE_ROOTED),
//        isEmulator = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_EMULATOR_RUNNING),
//        isVirtual = getSecurityCheck(SecurityCheckManager.SecurityCheck.IS_VIRTUAL_ENV_RUNNING),
//        isMalwareDetected = false // TODO: this property is  false
//    )
//
//
//@OptIn(ExperimentalStdlibApi::class)
//fun Context.getApkChecksum(): String? {
//    try {
//        // Get the APK file of the app
//        val apkPath = this.packageCodePath
//        val apkFile = File(apkPath)
//
//        // Create a MessageDigest instance for SHA-256
//        val digest = MessageDigest.getInstance("SHA-256")
//
//        // Read the APK file and update the digest
//        FileInputStream(apkFile).use { inputStream ->
//            val buffer = ByteArray(4096)
//            var bytesRead: Int
//            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                digest.update(buffer, 0, bytesRead)
//            }
//        }
//
//        // Convert the digest to a hex string
//        val hash = digest.digest()
//        return hash.toHexString()
//    } catch (e: Exception) {
//        e.printStackTrace()
//    }
//    return null
//}
//
//// global scope attestation loop because we want it to run as long as the process is running
//fun loopRequestDeviceAttestation(sharedPreferences: NearPaySharedPreferences, requestDeviceAttestation: RequestDeviceAttestation) = GlobalScope.launch(Dispatchers.IO) {
//    while (isActive) {
//        if (sharedPreferences.isLogin()) {
//            requestDeviceAttestation()
//        }
//        val delayMinutes = 60 * 1000 * 60 // 1 hour
//        Timber.d("Attestation delayed for ${delayMinutes / 60000} minutes")
//        delay(delayMinutes.toLong())
//    }
//}