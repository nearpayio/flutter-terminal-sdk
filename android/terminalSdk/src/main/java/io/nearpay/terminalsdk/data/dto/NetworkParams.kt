package io.nearpay.terminalsdk.data.dto

import io.nearpay.softpos.utils.Environment

data class NetworkParams(
    val environment: Environment,
    val country: Country,
)