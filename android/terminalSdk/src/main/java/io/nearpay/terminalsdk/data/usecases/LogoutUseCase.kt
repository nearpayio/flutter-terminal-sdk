//package io.nearpay.terminalsdk.data.usecases
//
//import timber.log.Timber
//import io.nearpay.terminalsdk.data.remote.ServerSwitcher
//import io.nearpay.terminalsdk.data.remote.PosServiceApi
//import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
////import io.nearpay.terminalsdk.data.local.NearpayDatabase
//
//class LogoutUseCase (
//    private val serverSwitcher: ServerSwitcher<PosServiceApi>,
//    private val sharedPreferences: TerminalSharedPreferences,
//    private val database: NearpayDatabase
//) {
//
//    suspend operator fun invoke(): Boolean {
//
//
//        if (sharedPreferences.isLogin()) {
//            try {
//                serverSwitcher.getApiService().logout()
//                Tracker.event("user_logging_out_succeeded")
//            } catch (throwable: Throwable) {
//                Tracker.event("user_logging_out_failed")
//                Timber.d(throwable)
//            }
//        }
//
//        database.clearAllTables()
//        sharedPreferences.getCurrentUser()?.let { sharedPreferences.removeUserFromList(it) }
//        val isSharedPreferencesCleaned = sharedPreferences.destroy()
//        val isSpinEmvClosed = SpinActions.close?.invoke() ?: true // if close.invoke() is null, then it means that it means SpinInstance has not been initialized yet, so there's nothing to close
//        Tracker.event("user_data_cleared")
//        Tracker.clearUser()
//        return isSharedPreferencesCleaned && isSpinEmvClosed
//    }
//}