//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.terminalsdk.data.dto.TransactionBanner
//import io.nearpay.terminalsdk.data.dto.TransactionBannerList
//import io.nearpay.terminalsdk.data.remote.ApiResponse
//import io.nearpay.terminalsdk.data.remote.ServerSwitcher
//import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
//import io.nearpay.terminalsdk.data.remote.safeApiCall
//import java.text.SimpleDateFormat
//import java.util.*
//
//class GetReversibleTransactionsUseCase(
//    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
//
//    suspend operator fun invoke(page: Int, pageSize: Int) : ApiResponse<TransactionBannerList> {
//
//        val result = safeApiCall {
//            serverSwitcher.getApiService().getTransactionsList(
//                page = page,
//                limit = pageSize,
//                isReconciled = false
//            )
//        }
//        return when (result) {
//            is ApiResponse.Success -> {
//                if (result.items.transactionBanners.isNotEmpty()) {
//                    val reversibleTransactions = result.items.transactionBanners.filter {
//                        it.isReversible() && !it.is_reversed
//                    }
//
//                    ApiResponse.Success(TransactionBannerList(0, reversibleTransactions))
//                } else
//                    ApiResponse.Success(TransactionBannerList(0, emptyList()))
//            }
//
//            is ApiResponse.Error ->
//                ApiResponse.Error(result.requestException)
//        }
//    }
////
////         safeApiCall {
////            serverSwitcher.getApiService().getTransactionsList(
////                page = page,
////                limit = pageSize,
////                isReconciled = false)
////        }.let {
////            when (it) {
////                is ApiResponse.Error ->
////                    ApiResponse.Error(it.requestException)
////                is ApiResponse.Success -> {
////                    if (it.items.transactionBanners.isNotEmpty()) {
////                        val reversibleTransactions = it.items.transactionBanners.filter {
////                            it.isReversible() && !it.is_reversed
////                        }
////
////                        ApiResponse.Success(TransactionBannerList(0, reversibleTransactions))
////                    } else
////                        ApiResponse.Success(TransactionBannerList(0, emptyList()))
////                }
////            }
////        }
////    }
////
////            safeApiCall {
////        serverSwitcher.getApiService().getTransactionsList(
////            page = page,
////            limit = pageSize,
////            isReconciled = false)
////    }
////    { it ->
////
////        when (it) {
////            is ApiResponse.Error ->
////                ApiResponse.Error(it.requestException)
////            is ApiResponse.Success -> {
////                if (it.items.transactionBanners.isNotEmpty()) {
////                    val reversibleTransactions = it.items.transactionBanners.filter {
////                        it.isReversible() && !it.is_reversed
////                    }
////
////                    ApiResponse.Success(TransactionBannerList(0, reversibleTransactions))
////                } else
////                    ApiResponse.Success(TransactionBannerList(0, emptyList()))
////            }
////        }
////    }
//
//    private fun TransactionBanner.isReversible(): Boolean {
//        val oneMinute = 1000 * 60L
//
//        val endDateTime = "$start_date $start_time"
//        val endTimeStamp = try {
//            SimpleDateFormat("dd/MM/yy HH:mm:ss", Locale.ENGLISH).parse(endDateTime) ?: return false
//        } catch (e: Exception) {
//            return false
//        }
//        val differ = Date().time - endTimeStamp.time
//        return differ < oneMinute
//    }
//}
//
//
