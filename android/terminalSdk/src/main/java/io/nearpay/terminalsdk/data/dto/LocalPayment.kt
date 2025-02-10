package io.nearpay.terminalsdk.data.dto

// TODO add ROOM SQLite db
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.Index
//import androidx.room.PrimaryKey
//import io.nearpay.softpos.core.utils.enums.TransactionType
//import kotlinx.serialization.Serializable
//
//@Serializable
//@Entity(tableName = "local_payment_table", indices = [Index(value = ["requestId", "terminalID"])])
//data class LocalPayment(
//
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    val id: Long = 0,
//
//    @ColumnInfo(name = "requestId")
//    var requestId: String? = "",
//
//    @ColumnInfo(name = "terminalID")
//    var terminalID: String,
//
//    @ColumnInfo(name = "transactionUuid")
//    var transactionUuid: String? = "",
//
//    @ColumnInfo(name = "amount")
//    var amount: Long? = 0L,
//
//    @ColumnInfo(name = "payment")
//    var payment: Payment = Payment(null, null, null, null, "", null, null, "", "", "", ""),
//
//    @ColumnInfo(name = "transactionType")
//    var transactionType: TransactionType = TransactionType.UNDEFINED,
//
//    @ColumnInfo(name = "receipts")
//    var receipts: List<TransactionReceipt>? = emptyList(),
//
//    @ColumnInfo(name = "timestamp")
//    var timestamp: Long = 0,
//
//    @ColumnInfo(name = "retry")
//    var retry: Int = 3,
//
//    @ColumnInfo(name = "localPaymentState")
//    val localPaymentState: LocalPaymentState,
//
//    @ColumnInfo(name = "networkType")
//    val networkType: String? = null
//)
//
//@Serializable
//enum class LocalPaymentState {
//    NEW, READ, RECEIVED, UNCONFIRMED, UNRECEIVED, REVERSED
//}