package io.nearpay.softpos.core.utils.enums

enum class ProxyType(val id: Int){
    NONE(-1),
    WEB_SOCKET(0),
    USB(1);

    companion object {
        fun of(id: Int): ProxyType {
            return when(id) {
               -1 -> NONE
               0 -> WEB_SOCKET
               1 -> USB
               else -> NONE
            }
        }
    }
}