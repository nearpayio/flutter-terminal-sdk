//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.terminalsdk.data.dto.TransactionBanner
//import io.nearpay.terminalsdk.data.dto.TransactionBannerList
//import io.nearpay.softpos.core.data.remote.*
//import io.nearpay.softpos.core.di.ServerSwitcher
//import io.nearpay.softpos.core.utils.enums.TransactionType
//import javax.inject.Inject
//import javax.inject.Named
//
//class GetRefundableTransactionsUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
//    suspend operator fun invoke(page: Int, pageSize: Int) = safeApiCall {
//        serverSwitcher.getApiService().getTransactionsList(
//            page = page,
//            limit = pageSize,
//            isReconciled = false)
//    }.let { it ->
//
//        when (it) {
//            is ApiResponse.Error ->
//                ApiResponse.Error(it.requestException)
//            is ApiResponse.Success -> {
//                if (it.items.transactionBanners.isNotEmpty()) {
//                    val refundableTransactions = it.items.transactionBanners.filter {
//                        it.isRefundable() && !it.is_reversed
//                    }
//
//                    ApiResponse.Success(
//                        TransactionBannerList(
//                            0,
//                            refundableTransactions
//                        )
//                    )
//                } else
//                    ApiResponse.Success(
//                        TransactionBannerList(
//                            0,
//                            emptyList()
//                        )
//                    )
//            }
//        }
//    }
//
//    private fun TransactionBanner.isRefundable(): Boolean {
//        val isRefundTransactionType = transaction_type.id == TransactionType.REFUND.id
//        return is_approved && !isRefundTransactionType && !is_reversed
//    }
//}
//
//
