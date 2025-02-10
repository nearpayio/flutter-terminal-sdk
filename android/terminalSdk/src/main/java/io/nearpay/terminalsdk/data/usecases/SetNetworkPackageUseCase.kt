//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.softpos.core.data.local.NearPaySharedPreferences
//import javax.inject.Inject
//
//class SetNetworkPackageUseCase @Inject constructor(
//    private val sharedPreferences: NearPaySharedPreferences
//) {
//
//    operator fun invoke(clientPackageName: String): Boolean {
//        return sharedPreferences.saveNetworkPackageName(clientPackageName)
//    }
//}