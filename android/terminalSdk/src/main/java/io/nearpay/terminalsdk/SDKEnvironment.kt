package io.nearpay.terminalsdk

import io.nearpay.softpos.utils.Environment


enum class SdkEnvironment {
    SANDBOX,
    PRODUCTION;

    fun toEnvironment(): Environment {
        return when (this) {
            SANDBOX -> Environment.SANDBOX
            PRODUCTION -> Environment.PRODUCTION
        }
    }
}