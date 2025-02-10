package io.nearpay.softpos.core.utils.enums


sealed class PaymentFailure {
    object PaymentCanceled: PaymentFailure()
    object PaymentCanceledWithReverse: PaymentFailure()
//    data class PaymentDeclined(val receipts: List<TransactionReceipt>?): PaymentFailure()
    data class PaymentRejected(val message: String): PaymentFailure()
    data class TerminalNotReady(val message: String): PaymentFailure()
    object TerminalIsBusy: PaymentFailure()
    object InvalidArgument: PaymentFailure()
}