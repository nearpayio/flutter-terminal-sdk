//package io.nearpay.softpos.core.utils.managers
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.bluetooth.BluetoothAdapter
//import android.bluetooth.BluetoothDevice
//import android.bluetooth.BluetoothManager
//import android.bluetooth.BluetoothSocket
//import android.bluetooth.le.ScanFilter
//import android.bluetooth.le.ScanResult
//import android.companion.AssociationRequest
//import android.companion.BluetoothDeviceFilter
//import android.companion.BluetoothLeDeviceFilter
//import android.companion.CompanionDeviceManager
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.content.IntentSender
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Handler
//import android.os.Looper
//import android.os.ParcelUuid
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.annotation.ChecksSdkIntAtLeast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.content.ContextCompat
//import androidx.core.content.IntentCompat
//import androidx.fragment.app.Fragment
//import io.nearpay.terminalsdk.BluetoothObserver
//import timber.log.Timber
//
//object BluetoothManager {
//
//    const val REQUEST_CODE_BLUETOOTH_PERMISSION = 110
//    const val RESULT_BLUETOOTH_PERMISSION_GRANTED = 1
//    const val RESULT_BLUETOOTH_PERMISSION_DENIED = 0
//
//    private var bluetoothDevice: BluetoothDevice? = null
//    private var bluetoothAdapter: BluetoothAdapter? = null
//    private var socket: BluetoothSocket? = null
//
//    private var bluetoothObserver: BluetoothObserver? = null
//
//    var bluetoothDeviceName: String = ""
//    var isBluetoothDeviceConnected: Boolean = false
//
//
//    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
//    private val isAndroidTwelveOrNewer = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
//
//    private val Context.isBluetoothScanPermissionDenied
//        get() = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_DENIED
//
//    private val Context.isBluetoothConnectPermissionDenied
//        get() = ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED
//
//    val Fragment.registerRequestEnableBluetoothActivityResult
//        get() = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { activityResult ->
//            if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
//                BluetoothManager(requireContext(), bluetoothObserver)
//            } else {
//                bluetoothDeviceName = ""
//                isBluetoothDeviceConnected = false
//                bluetoothObserver?.onBluetoothPermissionsDenied()
//            }
//        }
//
//    val Fragment.registerRequestBluetoothPermissionsActivityResult
//        get() = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) { activityResult ->
//            when (activityResult.resultCode) {
//                RESULT_BLUETOOTH_PERMISSION_GRANTED -> {
//                    BluetoothManager(requireContext(), bluetoothObserver)
//                }
//
//                RESULT_BLUETOOTH_PERMISSION_DENIED -> {
//                    bluetoothDeviceName = ""
//                    isBluetoothDeviceConnected = false
//                    bluetoothObserver?.onBluetoothPermissionsDenied()
//                }
//            }
//        }
//
//    val Fragment.registerAssociateWithDeviceActivityResult
//        @SuppressLint("MissingPermission")
//        get() = registerForActivityResult(
//            ActivityResultContracts.StartIntentSenderForResult()
//        ) { activityResult ->
//            if (activityResult.resultCode == Activity.RESULT_OK) {
//                val leBluetoothDevice = IntentCompat.getParcelableExtra(activityResult.data ?: Intent(), CompanionDeviceManager.EXTRA_DEVICE, ScanResult::class.java)
//                val classicBluetoothDevice = IntentCompat.getParcelableExtra(activityResult.data ?: Intent(), CompanionDeviceManager.EXTRA_DEVICE, BluetoothDevice::class.java)
//                val bluetoothDevice = leBluetoothDevice?.device ?: classicBluetoothDevice
//
//                if (bluetoothDevice?.bondState == BluetoothDevice.BOND_BONDED) {
//                    bluetoothDeviceName = bluetoothDevice.name
//                    isBluetoothDeviceConnected = true
//                    bluetoothObserver?.onDeviceConnected(bluetoothDevice.name)
//                    PrinterManager.initZebraPrinter(bluetoothDevice.address.orEmpty(), bluetoothDeviceName, requireActivity().applicationContext)
//                } else {
//                    startPairing(requireContext(), bluetoothDevice)
//                }
//
//            }
//        }
//
//    private val pairingReceiver = object : BroadcastReceiver() {
//        @SuppressLint("MissingPermission")
//        override fun onReceive(context: Context?, intent: Intent?) {
//            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED == intent?.action) {
//                when (bluetoothDevice?.bondState) {
//                    BluetoothDevice.BOND_BONDED -> {
//                        bluetoothDeviceName = bluetoothDevice?.name.orEmpty()
//                        isBluetoothDeviceConnected = true
//                        bluetoothObserver?.onDeviceConnected(bluetoothDevice?.name.orEmpty())
//                        PrinterManager.initZebraPrinter(bluetoothDevice?.address.orEmpty(), bluetoothDeviceName, context?.applicationContext)
//                    }
//
//                    BluetoothDevice.BOND_NONE -> {
//                        bluetoothDeviceName = ""
//                        bluetoothObserver?.onDeviceDisconnected()
//                    }
//                }
//            }
//        }
//    }
//
//    operator fun invoke(context: Context, btObserver: BluetoothObserver?) {
//        bluetoothObserver = btObserver
//
//        if (isAndroidTwelveOrNewer && (context.isBluetoothScanPermissionDenied || context.isBluetoothConnectPermissionDenied)) {
//            bluetoothObserver?.onRequestBluetoothPermissions()
//            return
//        }
//
//        bluetoothAdapter = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter
//
//        if (bluetoothAdapter?.isEnabled == false) {
//            val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            bluetoothObserver?.onRequestEnableBluetooth(enableBluetooth)
//            return
//        }
//
//        startDiscovery(context)
//    }
//
//    private fun startDiscovery(context: Context) {
//        val companionDeviceManager = context.getSystemService(CompanionDeviceManager::class.java)
//
//        val classicBluetoothDeviceFilter = BluetoothDeviceFilter.Builder()
//            .build()
//
//        val leBluetoothDeviceFilter = BluetoothLeDeviceFilter.Builder()
//            .build()
//
//        val pairingRequest = AssociationRequest.Builder()
//            .addDeviceFilter(classicBluetoothDeviceFilter)
//            .addDeviceFilter(leBluetoothDeviceFilter)
//            .setSingleDevice(false)
//            .build()
//
//        bluetoothObserver?.onFindingDevice()
//
//        companionDeviceManager?.associate(
//            pairingRequest,
//            object : CompanionDeviceManager.Callback() {
//                override fun onDeviceFound(chooserLauncher: IntentSender) {
//                    bluetoothObserver?.onDeviceFound(chooserLauncher)
//                }
//
//                override fun onFailure(error: CharSequence?) {
//                    bluetoothObserver?.onDeviceNotFound()
//                }
//            },
//            Handler(Looper.getMainLooper())
//        )
//
//    }
//
//    @SuppressLint("MissingPermission")
//    fun startPairing(context: Context, device: BluetoothDevice?) {
//        bluetoothDevice = device
//        bluetoothDevice?.createBond()
//        socket = bluetoothDevice?.createRfcommSocketToServiceRecord(bluetoothDevice?.uuids?.first()?.uuid)
//        context.registerReceiver(pairingReceiver, IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED))
//    }
//
//    @SuppressLint("MissingPermission")
//    fun disconnect() {
//        try {
//            socket?.close()
//        }
//        catch (t: Throwable) {
//            Timber.d(t)
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    fun clear(context: Context) {
//        bluetoothObserver = null
//        try {
//            context.unregisterReceiver(pairingReceiver)
//            bluetoothAdapter?.cancelDiscovery()
//        } catch (t: Throwable) {
//            Timber.d(t)
//        }
//    }
//
//}