package io.nearpay.terminalsdk.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.util.concurrent.HandlerExecutor
import okhttp3.Request
import okio.utf8Size
import org.json.JSONArray
import retrofit2.Invocation
import timber.log.Timber
import java.util.UUID
import java.util.concurrent.Executor

operator fun <T> JSONArray.iterator(): Iterator<T> =
    (0 until this.length()).asSequence().map { this.get(it) as T }.iterator()

fun String.isAscii() = length == utf8Size().toInt()

fun Context.retrieveMainExecutor(): Executor {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        mainExecutor
    } else {
        HandlerExecutor(mainLooper)
    }
}

fun String?.isValidUuid(): Boolean {
    if (this == null) return false
    return try {
        UUID.fromString(this)
        true
    } catch (exception: IllegalArgumentException) {
        false
    }
}

fun Fragment.finishActivitySafely() = try { requireActivity().finish() } catch (t: Throwable) { Timber.d(t) }

fun <T : Annotation> Request.getAnnotation(annotationClass: Class<T>): T? {
    return this.tag(Invocation::class.java)?.method()?.getAnnotation(annotationClass)
}

//fun resolveRefreshException(throwable: Throwable) {
//    val nearpayException = resolveError(throwable)
//    if (nearpayException is AuthenticationException) {
//        CoreAction.onNetworkError(AuthenticationException())
//    }
//}

fun Activity.hideKeyboard() = WindowCompat.getInsetsController(window, window.decorView).hide(
    WindowInsetsCompat.Type.ime())
fun Fragment.hideKeyboard() = WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).hide(WindowInsetsCompat.Type.ime())

fun Activity.showKeyboard() = WindowCompat.getInsetsController(window, window.decorView).show(WindowInsetsCompat.Type.ime())
fun Fragment.showKeyboard() = WindowCompat.getInsetsController(requireActivity().window, requireActivity().window.decorView).show(WindowInsetsCompat.Type.ime())


internal fun getAppVersion(context: Context): String {
    val packageManager: PackageManager = context.packageManager
    val packageName: String = context.packageName
    val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
    return packageInfo.versionName
}


fun String.findMatch( strings: List<String>): Boolean {
    return strings.any { this.contains(it) }
}
