package io.nearpay.terminalsdk.data.dto

import io.nearpay.softpos.data.dto.PaymentCardScheme as ReaderCorePaymentCardScheme

enum class PaymentScheme {
    MADA,
    VISA,
    MAESTRO,
    MASTERCARD,
    AMERICAN_EXPRESS,
    GCC_NET,
    UNION_PAY,
    JCB,
    DISCOVER,
    UNDEFINED;

    fun getScheme() : ReaderCorePaymentCardScheme {
        return when (this){
            MADA -> ReaderCorePaymentCardScheme.MADA
            VISA -> ReaderCorePaymentCardScheme.VISA
            MAESTRO -> ReaderCorePaymentCardScheme.MAESTRO
            MASTERCARD -> ReaderCorePaymentCardScheme.MASTERCARD
            AMERICAN_EXPRESS -> ReaderCorePaymentCardScheme.AMERICAN_EXPRESS
            GCC_NET -> ReaderCorePaymentCardScheme.GCC_NET
            UNION_PAY -> ReaderCorePaymentCardScheme.UNION_PAY
            JCB -> ReaderCorePaymentCardScheme.JCB
            DISCOVER -> ReaderCorePaymentCardScheme.DISCOVER
            UNDEFINED -> ReaderCorePaymentCardScheme.UNDEFINED
        }
    }
}