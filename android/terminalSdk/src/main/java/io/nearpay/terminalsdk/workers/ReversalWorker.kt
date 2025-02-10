//package io.nearpay.softpos.core.utils.workers
//
//import android.content.Context
//import androidx.hilt.work.HiltWorker
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import dagger.assisted.Assisted
//import dagger.assisted.AssistedInject
//import io.nearpay.softpos.core.data.dto.LocalPaymentState
//import io.nearpay.terminalsdk.data.dto.Reversal
//import io.nearpay.softpos.core.data.dto.WorkStatus
//import io.nearpay.softpos.core.data.local.NearPaySharedPreferences
//import io.nearpay.softpos.core.data.local.room.NearpayDao
//import io.nearpay.softpos.core.data.local.room.WorkDao
//import io.nearpay.softpos.core.data.remote.ApiResponse
//import io.nearpay.softpos.core.domain.PostReversalUseCase
//import io.nearpay.softpos.core.utils.Tracker
//
//@HiltWorker
//class ReversalWorker @AssistedInject constructor(
//    @Assisted context: Context,
//    @Assisted params: WorkerParameters,
//    private val sharedPreferences: NearPaySharedPreferences,
//    private val nearpayDao: NearpayDao,
//    private val postReversalUseCase: PostReversalUseCase,
//    private val workDao: WorkDao
//    ) : CoroutineWorker(context, params) {
//
//    val uuid = inputData.getString("uuid").orEmpty()
//
//    override suspend fun doWork(): Result {
//        val totalRunAttemptCount = workDao.getWorkRunAttemptCount(workUuid = id.toString()) + 1
//        workDao.updateWorkByWorkUuid(workUuid = id.toString(), runAttemptCount = totalRunAttemptCount)
//
//        if (runAttemptCount > 3) {
//            workDao.updateWorkByWorkUuid(workUuid = id.toString(), workStatus = WorkStatus.PENDING)
//            return Result.failure()
//        }
//
//        if (totalRunAttemptCount > 50) {
//            workDao.deleteWork(workUuid = id.toString())
//            return Result.failure()
//        }
//
//        return postReversalUseCase(Reversal(uuid, sharedPreferences.getLocation())).let {
//            when (it) {
//                is ApiResponse.Error -> {
//                    Tracker.event("remote_reverse_failed")
//                    workDao.updateWorkByWorkUuid(workUuid = id.toString(), workStatus = WorkStatus.RETRYING)
//                    Result.retry()
//                }
//
//                is ApiResponse.Success -> {
//                    Tracker.event("remote_reverse_succeeded")
//                    if (it.items.isNotEmpty()) {
//                        nearpayDao.updateLocalPayment(
//                            receipts = it.items,
//                            localPaymentState = LocalPaymentState.REVERSED,
//                            transactionUuid = it.items.first().transaction_uuid)
//                    }
//                    workDao.deleteWork(workUuid = id.toString())
//                    Result.success()
//                }
//            }
//        }
//    }
//
//}