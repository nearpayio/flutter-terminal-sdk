package io.nearpay.softpos.core.utils.enums

enum class LoginType(val id: Int) {
    UNDETERMINED(-1),
    OTP(1),
    JWT(2);

    companion object {
        fun getType(id: Int): LoginType {
            return when (id) {
                1 -> OTP
                2 -> JWT
                else -> UNDETERMINED
            }
        }
    }
}