package io.nearpay.terminalsdk.data.dto
import io.nearpay.softpos.library.Country as ReaderCoreCountry

enum class Country {
    SA,
    TR,
    USA;

    fun getCountry() :  ReaderCoreCountry {
        return when (this){
            SA ->  ReaderCoreCountry.SA
            USA ->  ReaderCoreCountry.USA
            TR ->  ReaderCoreCountry.TR
        }
    }
}
