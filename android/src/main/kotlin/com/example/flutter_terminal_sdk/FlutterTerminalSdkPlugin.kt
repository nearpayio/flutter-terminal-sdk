package com.example.flutter_terminal_sdk

import NearpayOperatorFactory
import android.app.Activity
import android.content.Context
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import com.example.flutter_terminal_sdk.common.filter.ArgsFilter
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import com.example.flutter_terminal_sdk.common.NearpayProvider
import timber.log.Timber

class FlutterTerminalSdkPlugin : FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private lateinit var channel: MethodChannel
    private var context: Context? = null
    private var activity: Activity? = null
    private lateinit var provider: NearpayProvider
    private var operatorFactory: NearpayOperatorFactory? = null

    private val PERMISSIONS_REQUEST_CODE = 0

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        Timber.plant(Timber.DebugTree());
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "nearpay_plugin")
        channel.setMethodCallHandler(this)
        provider = NearpayProvider(context!!, channel)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        provider.attachActivity(activity!!)
        operatorFactory = NearpayOperatorFactory(provider)
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onDetachedFromActivity() {
        activity = null
        provider.detachActivity()
        operatorFactory = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val argsFilter = ArgsFilter(call.arguments())

        when (call.method) {
            "initialize" -> {
                try {

                    if (!provider.isInitialized()) {
                        provider.initializeSdk(argsFilter)
                    }

                    checkAndRequestPermissions()
                    checkNfcAndWifiStatus()

                    if (operatorFactory == null) {
                        operatorFactory = NearpayOperatorFactory(provider)
                    }
                    Timber.d("Did initialize")
                    result.success("Initialization successful")
                } catch (e: Exception) {
                    result.error(
                        "INITIALIZATION_FAILED",
                        "Failed to initialize Nearpay plugin: ${e.message}",
                        null
                    )
                }
            }

            else -> {
                if (operatorFactory == null) {
                    result.error(
                        "PLUGIN_NOT_INITIALIZED",
                        "Nearpay plugin not initialized properly. Ensure you have called the initialize method first.",
                        null
                    )
                    return
                }

                val operation = operatorFactory!!.getOperation(call.method)

                if (operation != null) {
                    operation.run(argsFilter) { response ->
                        result.success(response)
                    }
                } else {
                    result.notImplemented()
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val missingPermissions = provider.checkRequiredPermissions()
            .filter { !it.isGranted }
            .map { it.permission }

        if (missingPermissions.isNotEmpty() && activity != null) {
            ActivityCompat.requestPermissions(
                activity!!,
                missingPermissions.toTypedArray(),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun checkNfcAndWifiStatus() {
        if (!provider.isNfcEnabled(activity!!)) {
            throw IllegalStateException("NFC is disabled. Please enable NFC.")
        }

        if (!provider.isWifiEnabled(activity!!)) {
            throw IllegalStateException("WiFi is disabled. Please enable WiFi.")
        }
    }
}
