package io.nearpay.softpos.core.location.listeners

import io.nearpay.softpos.core.location.ResolvableApiException

interface LocationSettingCallback {
    fun onResultResolutionRequired(resolvable: ResolvableApiException)
    fun onResultSettingsChangeUnavailable()
    fun onResultSuccess()
}
