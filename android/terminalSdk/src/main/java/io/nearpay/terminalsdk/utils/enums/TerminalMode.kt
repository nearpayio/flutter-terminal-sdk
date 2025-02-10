package io.nearpay.softpos.core.utils.enums

enum class TerminalMode(val id: String){
    POS("pos"),
    KIOSK("kiosk"),
    INBOX("inbox"),
    SEARCH("search");

    companion object {
        fun of(id: String): TerminalMode {
            return when(id) {
                "pos" -> POS
                "kiosk" -> KIOSK
                "inbox" -> INBOX
                "search" -> SEARCH
                else -> POS
            }
        }
    }
}