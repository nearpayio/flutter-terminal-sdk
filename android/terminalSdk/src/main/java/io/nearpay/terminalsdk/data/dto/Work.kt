package io.nearpay.terminalsdk.data.dto

// TODO add ROOM SQLite db
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.Index
//import androidx.room.PrimaryKey
//import kotlinx.serialization.Serializable
//
//@Serializable
//@Entity(tableName = "work_table", indices = [Index(value = ["workUuid"])])
//data class Work(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    val id: Long = 0,
//
//    @ColumnInfo(name = "transactionUuid")
//    var transactionUuid: String = "",
//
//    @ColumnInfo(name = "workUuid")
//    var workUuid: String = "",
//
//    @ColumnInfo(name = "runAttemptCount")
//    var runAttemptCount: Int = 0,
//
//    @ColumnInfo(name = "workStatus")
//    var workStatus: WorkStatus = WorkStatus.DEFAULT
//)
//
//@Serializable
//enum class WorkStatus {
//    DEFAULT, RETRYING, PENDING
//}