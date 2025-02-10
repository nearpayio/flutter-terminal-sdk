//package io.nearpay.terminalsdk.data.usecases
//
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import io.nearpay.terminalsdk.data.dto.TransactionBanner
//import io.nearpay.softpos.core.data.remote.*
//import io.nearpay.softpos.core.di.ServerSwitcher
//import io.nearpay.softpos.core.utils.enums.TransactionType
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//import javax.inject.Named
//
//class GetRefundableTransactionsPagingUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
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
//                            val refundableTransactions = it.items.transactionBanners.filter {
//                                it.isRefundable() && !it.is_reversed
//                            }
//
//                            PagingSourceResponse.Success(refundableTransactions, it.items.count)
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
//    private fun TransactionBanner.isRefundable(): Boolean {
//        val isRefundTransactionType = transaction_type.id == TransactionType.REFUND.id
//        return is_approved && !isRefundTransactionType && !is_reversed
//    }
//}