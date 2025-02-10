//package io.nearpay.terminalsdk.data.usecases
//
//import androidx.paging.Pager
//import androidx.paging.PagingConfig
//import io.nearpay.terminalsdk.data.dto.ReportMonth
//import io.nearpay.softpos.core.data.remote.*
//import io.nearpay.softpos.core.di.ServerSwitcher
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import javax.inject.Inject
//import javax.inject.Named
//
//class GetReportUseCase @Inject constructor(
//    @Named("TransactionServerSwitcher") private val serverSwitcher: ServerSwitcher<TransactionServiceApi>
//) {
//
//    suspend operator fun invoke() = withContext(Dispatchers.IO) {
//
//        val dataSource: (suspend (Int, Int) -> PagingSourceResponse<ReportMonth>) = { page: Int, pageSize: Int ->
//            safeApiCall {
//                serverSwitcher.getApiService().getReport(page, pageSize)
//            }.let {
//
//                when (it) {
//                    is ApiResponse.Error ->
//                        PagingSourceResponse.Error(it.requestException)
//
//                    is ApiResponse.Success ->
//                        if (it.items.months.isNotEmpty())
//                            PagingSourceResponse.Success(it.items.months, it.items.total)
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