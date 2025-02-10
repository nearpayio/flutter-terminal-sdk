//package io.nearpay.softpos.core.utils.managers
//
//import android.content.Context
//import android.os.Build
//import com.zebra.sdk.comm.BluetoothConnection
//import io.nearpay.softpos.core.utils.printer.CentermPrinter
//import io.nearpay.softpos.core.utils.printer.IminPrinter
//import io.nearpay.softpos.core.utils.printer.Printer
//import io.nearpay.softpos.core.utils.printer.SunmiPrinter
//import io.nearpay.softpos.core.utils.printer.WosulPrinter
//import io.nearpay.softpos.core.utils.printer.TelpoPrinter
//import io.nearpay.softpos.core.utils.printer.ZebraPrinter
//import io.nearpay.softpos.core.utils.printer.TrenditPrinter
//import java.lang.ref.WeakReference
//
//object PrinterManager {
//
//    var printer: Printer? = null
//
//    fun initZebraPrinter(macAddress: String, deviceName: String, context: Context?) {
//        if (printer != null) return
//
//        val bluetoothConnection = BluetoothConnection(macAddress)
//        ZebraPrinter.setZebraPaperWidthMmByDeviceName(deviceName)
//        printer = ZebraPrinter(bluetoothConnection, context)
//    }
//
//    fun Context.invokePrinterManager() {
//        if (printer != null) return
//
//        val weakContext = WeakReference(this)
//        val context = weakContext.get()
//            ?: return
//        printer = when {
//            "CTA Q7".isModel() -> CentermPrinter(context)
//            "Q6Pro".isModel() -> WosulPrinter(context)
//            "VSTC".isModel() || "V2s_STGL".isModel() -> SunmiPrinter(context)
//            "S1".isModel() -> IminPrinter(context)
//            "sprd".isManufacturer() && "S680".isModel() -> TrenditPrinter()
//            "alps".isManufacturer() && "P8".isModel() -> TelpoPrinter(context)
//            else -> null
//        }
//    }
//
//    private fun String.isModel() = Build.MODEL == this
//    private fun String.isManufacturer() = Build.MANUFACTURER == this
//}