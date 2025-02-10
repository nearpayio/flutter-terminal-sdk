//package io.nearpay.softpos.core.location
//
//import android.content.Context
//import android.provider.Settings
//import androidx.lifecycle.LifecycleOwner
//import androidx.work.ExistingPeriodicWorkPolicy
//import androidx.work.ExistingWorkPolicy
//import androidx.work.OneTimeWorkRequest
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.PeriodicWorkRequestBuilder
//import androidx.work.WorkInfo
//import androidx.work.WorkManager
//import io.nearpay.terminalsdk.data.dto.Location
//import io.nearpay.softpos.core.location.listeners.LocationSettingCallback
//import io.nearpay.softpos.core.location.workers.LocationSettingWorker
//import kotlinx.coroutines.flow.collectLatest
//import kotlinx.coroutines.flow.receiveAsFlow
//import timber.log.Timber
//import java.util.concurrent.TimeUnit
//
//object LocationFetcher {
//
//    private const val LOCATION_UPDATE_WORKER_TAG = "LocationUpdateWorkerTag"
//    private const val LOCATION_SETTING_WORKER_TAG = "LocationSettingWorkerTag"
//    private const val LOCATION_UPDATE_WORKER_NAME = "LocationUpdateWorkerName"
//    private const val LOCATION_SETTING_WORKER_NAME = "LocationSettingWorkerName"
//
//    fun checkDeviceLocationSettings(context: Context, lifecycleOwner: LifecycleOwner, callback: LocationSettingCallback) {
//
//        val locationSettingRequest: OneTimeWorkRequest =
//            OneTimeWorkRequestBuilder<LocationSettingWorker>().addTag(LOCATION_SETTING_WORKER_TAG)
//                .build()
//
//        WorkManager.getInstance(context).enqueueUniqueWork(
//            LOCATION_SETTING_WORKER_NAME,
//            ExistingWorkPolicy.REPLACE,
//            locationSettingRequest
//        )
//
//        WorkManager.getInstance(context).getWorkInfoByIdLiveData(locationSettingRequest.id)
//            .observe(lifecycleOwner) { workInfo ->
//                if (workInfo?.state == WorkInfo.State.SUCCEEDED) {
//
//                    val locationSettingType =
//                        workInfo.outputData.getInt(LocationSettingWorker.location_Setting_Type, 0)
//                    when (LocationSettingCallbackType.getType(locationSettingType)) {
//
//                        LocationSettingCallbackType.RESULT_RESOLUTION_REQUIRED -> {
//                            val resolvableApiException =
//                                LocationSettingWorker.exceptionChannel.value
//                            resolvableApiException?.let { callback.onResultResolutionRequired(it) }
//                        }
//
//                        LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE -> {
//                            callback.onResultSettingsChangeUnavailable()
//                        }
//
//                        LocationSettingCallbackType.RESULT_SUCCESS -> {
//                            callback.onResultSuccess()
//                        }
//
//                        else -> Unit
//                    }
//
//                    WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_SETTING_WORKER_TAG)
//
//                }
//            }
//    }
//
//    suspend fun startLocationUpdates(
//        context: Context,
//        onReceiveLocationAction: (Location) -> Unit
//    ) {
//
//        val locationRequest = PeriodicWorkRequestBuilder<LocationWorker>(15, TimeUnit.MINUTES)
//            .addTag(LOCATION_UPDATE_WORKER_TAG)
//            .build()
//
//        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//            LOCATION_UPDATE_WORKER_NAME,
//            ExistingPeriodicWorkPolicy.KEEP,
//            locationRequest
//        )
//
//        LocationWorker.locationChannel.receiveAsFlow().collectLatest {
//            if (it == null) {
//                return@collectLatest
//            }
//
//            Timber.i("Location received -->$it")
//           Location(
//                long = it.longitude,
//                lat = it.latitude,
//                isLocationFromMock = isLocationFromMockProvider(context, it)
//            ).also { result ->
//               Timber.i("isLocationFromMock -->${result.isLocationFromMock}")
//                onReceiveLocationAction(result)
//            }
//        }
//    }
//
//    fun stopLocationUpdates(context: Context) {
//        WorkManager.getInstance(context).cancelAllWorkByTag(LOCATION_UPDATE_WORKER_TAG)
//    }
//
//    @Suppress("DEPRECATION")
//    private fun isLocationFromMockProvider(
//        context: Context,
//        location: android.location.Location
//    ): Boolean {
//
//        val isMock = when {
//            android.os.Build.VERSION.SDK_INT in 26..31 -> location.isFromMockProvider
//            android.os.Build.VERSION.SDK_INT >= 31 -> location.isMock
//            else -> {
//                !Settings.Secure.getString(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")
//            }
//        }
//
//        return isMock
//    }
//
//}
//
