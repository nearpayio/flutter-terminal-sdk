package io.nearpay.softpos.core.utils.enums
import io.nearpay.terminalsdk.R

enum class CardScheme(val id: String, val iconRes: Int) {
    UNDEFINED("undefined", R.drawable.ic_payment_scheme_placeholder),
    MADA("P1", R.drawable.ic_mada_dark),
    VISA("VC", R.drawable.ic_visa_logo),
    MASTERCARD("MC", R.drawable.ic_mastercard),
    MAESTRO("DM", R.drawable.ic_maestro_logo),
    AMERICAN_EXPRESS("AX", R.drawable.ic_amex),
    GCC_NET("GN", R.drawable.ic_gcc_net),
    UNIONPAY("UP", R.drawable.ic_unionpay),
    JCB("JC", R.drawable.ic_jcb),
    DISCOVER("DC", R.drawable.ic_discover);

    companion object {
        fun getScheme(id: String) = values().firstOrNull {
            it.id == id
        } ?: UNDEFINED
    }
}