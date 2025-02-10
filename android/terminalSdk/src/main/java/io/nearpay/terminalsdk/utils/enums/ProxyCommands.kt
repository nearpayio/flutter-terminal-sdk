//package io.nearpay.softpos.core.utils.enums
//
//import io.nearpay.softpos.core.utils.listeners.CancelListener
//import io.nearpay.softpos.core.utils.listeners.GetReconciliationsListener
//import io.nearpay.softpos.core.utils.listeners.GetTransactionListener
//import io.nearpay.softpos.core.utils.listeners.GetTransactionsListener
//import io.nearpay.softpos.core.utils.listeners.LogoutListener
//import io.nearpay.softpos.core.utils.listeners.PaymentListener
//import io.nearpay.softpos.core.utils.listeners.ReconcileListener
//import io.nearpay.softpos.core.utils.listeners.ReversalListener
//import io.nearpay.softpos.core.utils.listeners.TransactionStateListener
//
//sealed class ProxyCommands {
//    data class PURCHASE(
//        val amount: Long,
//        val customerReferenceNumber: String,
//        val requestId: String?,
//        val enableReceiptUi: Boolean,
//        val enableReversal: Boolean,
//        val finishTimeOut: Long,
//        val listener: PaymentListener,
//        val stateListener: TransactionStateListener
//    ): ProxyCommands()
//
//    data class REFUND(
//        val adminPin: String,
//        val amount: Long,
//        val customerReferenceNumber: String,
//        val transactionUuid: String,
//        val requestId: String?,
//        val enableReceiptUi: Boolean,
//        val enableReversal: Boolean,
//        val enableEditableRefundAmountUi: Boolean,
//        val finishTimeOut: Long,
//        val listener: PaymentListener,
//        val stateListener: TransactionStateListener
//    ): ProxyCommands()
//
//    data class RECONCILE(
//        val reconcileUuid: String,
//        val adminPin: String,
//        val enableReceiptUi: Boolean,
//        val finishTimeOut: Long,
//        val listener: ReconcileListener
//    ): ProxyCommands()
//
//    data class REVERSE(
//        val adminPin: String,
//        val transactionUuid: String,
//        val enableReceiptUi: Boolean,
//        val finishTimeOut: Long,
//        val listener: ReversalListener
//    ): ProxyCommands()
//
//    data class GetTransactionsList(
//        val page: Int,
//        val pageSize: Int,
//        val startDate: String? = null,
//        val endDate: String? = null,
//        val listener: GetTransactionsListener
//    ): ProxyCommands()
//
//    data class GetTransaction(
//        val transactionUuid: String,
//        val listener: GetTransactionListener
//    ): ProxyCommands()
//
//    data class GetReconciliationsList(
//        val page: Int,
//        val pageSize: Int,
//        val startDate: String? = null,
//        val endDate: String? = null,
//        val listener: GetReconciliationsListener
//    ): ProxyCommands()
//
//    data class GetReconciliation(
//        val reconciliationUuid: String,
//        val listener: ReconcileListener
//    ): ProxyCommands()
//
//    data class Logout(val listener: LogoutListener): ProxyCommands()
//
//    data class Cancel(
//        val listener: CancelListener? = null
//    ): ProxyCommands()
//}