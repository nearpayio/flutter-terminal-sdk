package io.nearpay.softpos.core.location

enum class LocationSettingCallbackType(val id: Int) {
    UNKNOWN(0),
    RESULT_RESOLUTION_REQUIRED(1),
    RESULT_SETTINGS_CHANGE_UNAVAILABLE(2),
    RESULT_SUCCESS(3);

    companion object {
        fun getType(id: Int): LocationSettingCallbackType {
            return when (id) {
                1 -> RESULT_RESOLUTION_REQUIRED
                2 -> RESULT_SETTINGS_CHANGE_UNAVAILABLE
                3 -> RESULT_SUCCESS
                else -> UNKNOWN
            }
        }
    }
}