package io.nearpay.softpos.core.utils.managers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object BaselineManager {

    private val _baselineFailed = MutableStateFlow(false)
    val baselineFailed = _baselineFailed.asStateFlow()

    fun onBaseLineSucceed() {
        _baselineFailed.value = false
    }

    fun onBaseLineFailed() {
        _baselineFailed.value = true
    }
}