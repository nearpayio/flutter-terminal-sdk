package io.nearpay.terminalsdk.data.dto

data class ServerErrorResponse(
    val statusCode: Int,
    val message: Map<String, List<String>>
)