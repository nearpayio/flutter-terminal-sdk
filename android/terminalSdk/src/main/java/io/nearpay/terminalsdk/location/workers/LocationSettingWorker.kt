//package io.nearpay.softpos.core.location.workers
//
//import android.content.Context
//import android.location.LocationManager
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import androidx.work.workDataOf
//import com.google.android.gms.tasks.Task
//import io.nearpay.softpos.common.utils.MobileServices
//import io.nearpay.softpos.common.utils.MobileServicesUtils.getMobileService
//import io.nearpay.softpos.core.location.GoogleResolvableApiException
//import io.nearpay.softpos.core.location.HMSResolvableApiException
//import io.nearpay.softpos.core.location.LocationSettingCallbackType
//import io.nearpay.softpos.core.location.ResolvableApiException
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import com.google.android.gms.common.api.ApiException as GApiException
//import com.google.android.gms.location.LocationRequest as GLocationRequest
//import com.google.android.gms.location.LocationServices as GLocationServices
//import com.google.android.gms.location.LocationSettingsRequest as GLocationSettingsRequest
//import com.google.android.gms.location.LocationSettingsResponse as GLocationSettingsResponse
//import com.google.android.gms.location.Priority as GPriority
//import com.huawei.hms.common.ApiException as HApiException
//import com.huawei.hms.location.LocationRequest as HLocationRequest
//import com.huawei.hms.location.LocationServices as HLocationServices
//import com.huawei.hms.location.LocationSettingsRequest as HLocationSettingsRequest
//import com.huawei.hms.location.LocationSettingsStatusCodes as HLocationSettingsStatusCodes
//
//class LocationSettingWorker(
//    appContext: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(appContext, workerParams) {
//
//    companion object {
//        val exceptionChannel = MutableStateFlow<ResolvableApiException?>(null)
//        const val location_Setting_Type = "LocationSettingType"
//    }
//
//    private val job = Job()
//    private val scope = CoroutineScope(Dispatchers.IO + job)
//    private val locationSettingCallbackChannel = Channel<LocationSettingCallbackType>()
//
//    override suspend fun doWork(): Result {
//
//        return try {
//            val result: LocationSettingCallbackType = when (applicationContext.getMobileService()) {
//                MobileServices.GCM -> checkGCMLocationSetting()
//                MobileServices.HMS -> checkHMSLocationSetting()
//                MobileServices.Other -> checkOtherLocationSetting()
//            }
//
//            Result.success(workDataOf(location_Setting_Type to result.id))
//        } catch (e: Throwable) {
//            Result.failure()
//        }
//    }
//
//    private suspend fun checkHMSLocationSetting(): LocationSettingCallbackType {
//
//        val locationRequest = HLocationRequest.create()
//            .setInterval(3000)
//            .setExpirationDuration(3000)
//            .setPriority(HLocationRequest.PRIORITY_HIGH_ACCURACY)
//
//        val locationSettingsRequest = HLocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//            .setAlwaysShow(true)
//            .build()
//
//        fun onSuccess() {
//            scope.launch {
//                locationSettingCallbackChannel.send(LocationSettingCallbackType.RESULT_SUCCESS)
//            }
//        }
//
//        fun onError(exception: Exception) {
//            Timber.d("error message:$exception")
//
//            val result: LocationSettingCallbackType = when {
//                exception !is HApiException -> {
//                    LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE
//                }
//
//                exception.statusCode != HLocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                    LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE
//                }
//
//                else -> {
//                    try {
//                        scope.launch {
//                            val hmsResolvableApiException =
//                                ResolvableApiException(exception as HMSResolvableApiException)
//                            exceptionChannel.emit(hmsResolvableApiException)
//                        }
//                        LocationSettingCallbackType.RESULT_RESOLUTION_REQUIRED
//                    } catch (_: Throwable) {
//                        LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE
//                    }
//                }
//            }
//
//            scope.launch { locationSettingCallbackChannel.send(result) }
//        }
//
//        HLocationServices.getSettingsClient(applicationContext)
//            .checkLocationSettings(locationSettingsRequest)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onError(it) }
//
//        return locationSettingCallbackChannel.receive()
//    }
//
//    private suspend fun checkGCMLocationSetting(): LocationSettingCallbackType {
//
//        val locationRequest = GLocationRequest.create()
//            .setInterval(3000)
//            .setExpirationDuration(3000)
//            .setPriority(GPriority.PRIORITY_HIGH_ACCURACY)
//
//        val locationSettingsRequest = GLocationSettingsRequest.Builder()
//            .addLocationRequest(locationRequest)
//            .setAlwaysShow(true)
//            .build()
//
//        fun onSuccess(task: Task<GLocationSettingsResponse>) {
//            task.getResult(GApiException::class.java)
//            scope.launch {
//                locationSettingCallbackChannel.send(LocationSettingCallbackType.RESULT_SUCCESS)
//            }
//        }
//
//        fun onError(exception: Exception) {
//            Timber.d("error message:$exception")
//
//            val result: LocationSettingCallbackType = when {
//                exception !is GApiException -> {
//                    LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE
//                }
//
//                exception.statusCode != HLocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
//                    LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE
//                }
//
//                else -> {
//                    try {
//                        scope.launch {
//                            val googleResolvableApiException =
//                                ResolvableApiException(exception as GoogleResolvableApiException)
//                            exceptionChannel.emit(googleResolvableApiException)
//                        }
//                        LocationSettingCallbackType.RESULT_RESOLUTION_REQUIRED
//                    } catch (_: Throwable) {
//                        LocationSettingCallbackType.RESULT_SETTINGS_CHANGE_UNAVAILABLE
//                    }
//                }
//            }
//
//            scope.launch { locationSettingCallbackChannel.send(result) }
//        }
//
//        GLocationServices.getSettingsClient(applicationContext)
//            .checkLocationSettings(locationSettingsRequest)
//            .addOnCompleteListener { task ->
//                try {
//                    onSuccess(task)
//                } catch (e: GApiException) {
//                    onError(e)
//                }
//            }
//
//        return locationSettingCallbackChannel.receive()
//
//    }
//
//    private suspend fun checkOtherLocationSetting(): LocationSettingCallbackType {
//
//        val locationManager =
//            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
//
//        if (
//            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
//            || locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
//        ) {
//            scope.launch { locationSettingCallbackChannel.send(LocationSettingCallbackType.RESULT_SUCCESS) }
//        } else {
//            scope.launch {
//            locationSettingCallbackChannel.send(LocationSettingCallbackType.RESULT_RESOLUTION_REQUIRED)
//            exceptionChannel.emit(ResolvableApiException(Exception()))}
//        }
//
//        return locationSettingCallbackChannel.receive()
//    }
//}