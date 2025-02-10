package io.nearpay.softpos.core.utils.enums

enum class TerminalLogoLayout(val id: String){
    HORIZONTAL("horizontal"),
    SQUARE("square");

    companion object {
        fun of(id: String): TerminalLogoLayout {
            return when(id) {
                "horizontal" -> HORIZONTAL
                "square" -> SQUARE
                else -> SQUARE
            }
        }
    }
}