//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.terminalsdk.data.dto.TransactionBanner
//import io.nearpay.terminalsdk.data.remote.ApiResponse
//import io.nearpay.terminalsdk.data.remote.ServerSwitcher
//import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
//import io.nearpay.terminalsdk.data.remote.safeApiCall
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import java.text.SimpleDateFormat
//import java.util.*
//
//class GetReversibleTransactionsPagingUseCase (
//    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
//
//    suspend operator fun invoke() = withContext(Dispatchers.IO) {
//
//        val dataSource: (suspend (Int, Int) -> PagingSourceResponse<TransactionBanner>) = { page: Int, pageSize: Int ->
//            safeApiCall {
//                serverSwitcher.getApiService().getTransactionsList(
//                    page = page,
//                    limit = pageSize,
//                    isReconciled = false)
//            }.let { it ->
//
//                when (it) {
//                    is ApiResponse.Error ->
//                        PagingSourceResponse.Error(it.requestException)
//
//                    is ApiResponse.Success -> {
//
//                        if (it.items.transactionBanners.isNotEmpty()) {
//                            val reversibleTransactions = it.items.transactionBanners.filter {
//                                it.isReversible() && !it.is_reversed
//                            }
//
//                            PagingSourceResponse.Success(reversibleTransactions, it.items.count)
//                        } else
//                            PagingSourceResponse.EmptyResponse
//                    }
//                }
//            }
//        }
//
//        Pager(
//            config = PagingConfig(pageSize = Paging.PageSize, enablePlaceholders = false),
//            pagingSourceFactory = { Paging(dataSource) }
//        ).flow
//
//    }
//
//     private fun TransactionBanner.isReversible(): Boolean {
//        val oneMinute = 1000 * 60L
//
//        val endDateTime = "$start_date $start_time"
//        val endTimeStamp = try{
//            SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.ENGLISH).parse(endDateTime) ?: return false
//        } catch (e: Exception){
//            return false
//        }
//        val differ = Date().time - endTimeStamp.time
//        return differ < oneMinute
//    }
//}