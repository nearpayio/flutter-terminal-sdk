package io.nearpay.softpos.core.location

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.provider.Settings
import timber.log.Timber

typealias GoogleResolvableApiException = com.google.android.gms.common.api.ResolvableApiException
typealias HMSResolvableApiException = com.huawei.hms.common.ResolvableApiException

class ResolvableApiException(
    private val resolvableApiException: Exception
) {
    @Throws(SendIntentException::class)
    fun startResolutionForResult(activity: Activity, code: Int) {
        when (resolvableApiException) {
            is HMSResolvableApiException -> {
                resolvableApiException.startResolutionForResult(activity, code)
            }
            is GoogleResolvableApiException -> {
                resolvableApiException.startResolutionForResult(activity, code)
            }
            else -> {
                try {
                    activity.startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),code)
                } catch (e:Throwable){
                    Timber.d(e.message)
                }
            }
        }
    }

}
