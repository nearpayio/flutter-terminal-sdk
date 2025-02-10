//package io.nearpay.terminalsdk.data.usecases
//
//import io.nearpay.terminalsdk.data.dto.ReconciliationItem
//import io.nearpay.terminalsdk.data.remote.ApiResponse
//import io.nearpay.terminalsdk.data.remote.ServerSwitcher
//import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
//import io.nearpay.terminalsdk.data.remote.safeApiCall
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
//class GetReconciliationListPagingUseCase(
//    private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
//
//    suspend operator fun invoke() = withContext(Dispatchers.IO) {
//
//        val dataSource: (suspend (Int, Int) -> PagingSourceResponse<ReconciliationItem>) = { page: Int, pageSize: Int ->
//
//            safeApiCall {
//                serverSwitcher.getApiService().getReconcile(page, pageSize)
//            }.let {
//
//                when (it) {
//                    is ApiResponse.Error ->
//                        PagingSourceResponse.Error(it.requestException)
//
//                    is ApiResponse.Success ->
//                        if (it.items.reconciliations.isNotEmpty())
//                            PagingSourceResponse.Success(it.items.reconciliations, it.items.total)
//                        else
//                            PagingSourceResponse.EmptyResponse
//                }
//            }
//        }
//
//        Pager(
//            config = PagingConfig(pageSize = Paging.PageSize, enablePlaceholders = false),
//            pagingSourceFactory = { Paging(dataSource) }
//        ).flow
//    }
//}