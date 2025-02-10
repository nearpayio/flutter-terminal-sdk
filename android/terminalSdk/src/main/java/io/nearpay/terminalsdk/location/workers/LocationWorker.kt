//package io.nearpay.softpos.core.location.workers
//
//import android.content.Context
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import android.os.Build
//import android.os.Bundle
//import android.os.Looper
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.Job
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import java.util.function.Consumer
//import com.google.android.gms.location.FusedLocationProviderClient as GFusedLocationProviderClient
//import com.google.android.gms.location.LocationCallback as GLocationCallback
//import com.google.android.gms.location.LocationRequest as GLocationRequest
//import com.google.android.gms.location.LocationResult as GLocationResult
//import com.google.android.gms.location.LocationServices as GLocationServices
//import com.google.android.gms.location.Priority as GPriority
//import com.huawei.hms.location.FusedLocationProviderClient as HFusedLocationProviderClient
//import com.huawei.hms.location.LocationCallback as HLocationCallback
//import com.huawei.hms.location.LocationRequest as HLocationRequest
//import com.huawei.hms.location.LocationResult as HLocationResult
//import com.huawei.hms.location.LocationServices.getFusedLocationProviderClient as HGetFusedLocationProviderClient
//import io.nearpay.softpos.core.location.LocationManager as NearPayLocationManager
//
//
//class LocationWorker(
//    appContext: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(appContext, workerParams) {
//
//    companion object {
//        val locationChannel = Channel<Location?>()
//    }
//
//    private val job = Job()
//    private val scope = CoroutineScope(Dispatchers.IO + job)
//
//    override suspend fun doWork(): Result {
//
//        val isPermissionGranted = NearPayLocationManager.checkPermissions(applicationContext)
//        val isLocationEnabled = NearPayLocationManager.isLocationEnabled(applicationContext)
//
//        if (!isPermissionGranted || !isLocationEnabled) {
//            Timber.i("Location permission missing or location is disabled")
//            return Result.failure()
//        }
//
//        return try {
//            applicationContext.firstMobileServiceAppeared(
//                google = { getGCMLocation() },
//                huawei = { getHMSLocation() },
//                others = { getOtherLocation() }
//            )
//
//            Result.success()
//        } catch (e: Throwable) {
//            Result.failure()
//        }
//    }
//
//    private val hLocationCallback = object : HLocationCallback() {
//
//        override fun onLocationResult(locationResult: HLocationResult?) {
//            locationResult?.let { result ->
//                if (result.locations.isNotEmpty()) {
//                    scope.launch { locationChannel.send(result.locations[0]) }
//                }
//            }
//        }
//    }
//
//    private val gLocationCallback = object : GLocationCallback() {
//        override fun onLocationResult(locationResult: GLocationResult) {
//            locationResult.let {
//                if (it.locations.isNotEmpty()) {
//                    scope.launch { locationChannel.send(it.locations[0]) }
//                }
//            }
//        }
//    }
//
//    @Throws(SecurityException::class)
//    fun getGCMLocation() {
//
//        val gLocationRequest = GLocationRequest.create().apply {
//            priority = GPriority.PRIORITY_HIGH_ACCURACY
//            numUpdates = 2
//        }
//
//        val fusedLocationProviderClient: GFusedLocationProviderClient =
//            GLocationServices.getFusedLocationProviderClient(applicationContext)
//
//        fusedLocationProviderClient.requestLocationUpdates(
//            gLocationRequest,
//            gLocationCallback,
//            Looper.getMainLooper()
//        ).addOnFailureListener { exception ->
//            getOtherLocation()
//            Timber.i(exception.message)
//        }
//    }
//
//    @Throws(SecurityException::class)
//    private fun getHMSLocation() {
//
//        val hLocationRequest = HLocationRequest().apply {
//            priority = HLocationRequest.PRIORITY_HIGH_ACCURACY
//            numUpdates = 2
//        }
//
//        val hmsLocationProviderClient: HFusedLocationProviderClient =
//            HGetFusedLocationProviderClient(applicationContext)
//
//        hmsLocationProviderClient.requestLocationUpdates(
//            hLocationRequest,
//            hLocationCallback,
//            Looper.getMainLooper()
//        ).addOnFailureListener { exception ->
//            getOtherLocation()
//            Timber.i(exception.message)
//        }
//    }
//
//    @Throws(SecurityException::class)
//    private fun getOtherLocation() {
//
//        val locationManager =
//            applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
//
//        if (locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true) {
//            scope.launch(Dispatchers.Main.immediate) {
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, object: LocationListener {
//                    override fun onLocationChanged(location: Location) {
//                        scope.launch {
//                            locationChannel.send(location)
//                        }
//                        locationManager.removeUpdates(this)
//                    }
//
//                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//
//                    override fun onProviderEnabled(provider: String) {}
//
//                    override fun onProviderDisabled(provider: String) {}
//                })
//            }
//        }
//
//        if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
//            scope.launch(Dispatchers.Main.immediate) {
//                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, object: LocationListener {
//                    override fun onLocationChanged(location: Location) {
//                        scope.launch {
//                            locationChannel.send(location)
//                        }
//                        locationManager.removeUpdates(this)
//                    }
//
//                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
//
//                    override fun onProviderEnabled(provider: String) {}
//
//                    override fun onProviderDisabled(provider: String) {}
//                })
//            }
//        }
//
//    }
//
//
//}