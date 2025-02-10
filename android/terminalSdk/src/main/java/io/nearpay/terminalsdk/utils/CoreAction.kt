package io.nearpay.terminalsdk.utils

import android.content.Context
import kotlin.properties.Delegates

object CoreAction {
    var isHiddenApp by Delegates.notNull<Boolean>()
    lateinit var getAppContext: () -> Context
    lateinit var getVersionCode: () -> Int
    lateinit var getVersionName: () -> String
    lateinit var getPackageName: () -> String
    lateinit var getPackageCore: () -> String
    lateinit var getLanguageCode: () -> String
    var getSdkVersionCode: (() -> String?)? = null
    var getProxyMetaData: (() -> String?)? = null
    lateinit var onNetworkError: (NearpayException) -> Unit
    lateinit var onSentryAction: (Throwable) -> Unit
}