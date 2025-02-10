//package io.nearpay.softpos.core.location
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import io.nearpay.terminalsdk.data.dto.Location
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.SupervisorJob
//import kotlinx.coroutines.cancelChildren
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.ensureActive
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.isActive
//import kotlinx.coroutines.launch
//import java.lang.ref.WeakReference
//import java.util.concurrent.atomic.AtomicBoolean
//
//object LocationManager {
//
//    private val _gpsLocationDisabled = MutableStateFlow(false)
//    val locationUnavailable = _gpsLocationDisabled.asStateFlow()
//
//    private val _locationPermissionMissing = MutableStateFlow(false)
//    val locationPermissionMissing = _locationPermissionMissing.asStateFlow()
//
//    private val job = SupervisorJob()
//    private val permissionScope = CoroutineScope(job + Dispatchers.IO)
//    private val locationUpdatesScope = CoroutineScope(job + Dispatchers.IO)
//
//    fun Context.invokeLocationManager(
//        updateLocationAction: (Location) -> Unit
//    ) {
//        val weakContext = WeakReference(this)
//
//        var isPermissionGranted: AtomicBoolean? = null
//        var isGpsEnabled: AtomicBoolean? = null
//
//        permissionScope.launch {
//            while (isActive) {
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
//                val newEnabledValue = isLocationEnabled(context)
//
//                if (newEnabledValue != isGpsEnabled?.get()) {
//                    isGpsEnabled?.set(newEnabledValue) ?: run {
//                        isGpsEnabled = AtomicBoolean(newEnabledValue)
//                    }
//
//                    onGpsEnableChanged(newEnabledValue)
//                }
//
//
//                delay(1000)
//                ensureActive()
//            }
//        }
//
//        locationUpdatesScope.launch {
//            while (isActive) {
//                val context = weakContext.get()
//                    ?: break
//
//                if (isPermissionGranted?.get() == true && isGpsEnabled?.get() == true) {
//                    LocationFetcher.startLocationUpdates(
//                        context = context,
//                        onReceiveLocationAction = {
//                            updateLocationAction(it)
//                        }
//                    )
//                } else {
//                    LocationFetcher.stopLocationUpdates(context = context)
//                }
//
//                delay(1000)
//                ensureActive()
//            }
//        }
//    }
//
//
//    fun onPermissionChanged(isGranted: Boolean) {
//        _locationPermissionMissing.value = !isGranted
//    }
//
//    fun onGpsEnableChanged(isEnabled: Boolean) {
//        _gpsLocationDisabled.value = !isEnabled
//    }
//
//    fun checkPermissions(context: Context): Boolean {
//        val accessCoarseLocation =
//            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
//        val accessFineLocation =
//            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
//
//        val isAccessCoarseLocationGranted =
//            accessCoarseLocation == PackageManager.PERMISSION_GRANTED
//        val isAccessFineLocationGranted = accessFineLocation == PackageManager.PERMISSION_GRANTED
//
//        return isAccessCoarseLocationGranted && isAccessFineLocationGranted
//    }
//
//    fun isLocationEnabled(context: Context): Boolean {
//        val locationManager =
//            ContextCompat.getSystemService(context, LocationManager::class.java) ?: return false
//
//        val isGpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        val isNetworkProviderEnabled =
//            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//
//        return isGpsProviderEnabled || isNetworkProviderEnabled
//    }
//
//    fun Context.stopLocationManager() {
//
//        LocationFetcher.stopLocationUpdates(this)
//        job.cancelChildren()
//    }
//}