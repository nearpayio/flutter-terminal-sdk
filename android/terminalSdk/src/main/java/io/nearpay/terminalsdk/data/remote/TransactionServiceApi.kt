package io.nearpay.terminalsdk.data.remote

import io.nearpay.softpos.data.remote.HealthCheckServiceApi
import io.nearpay.terminalsdk.data.dto.IsCanceled
import io.nearpay.terminalsdk.data.dto.ID
import io.nearpay.terminalsdk.data.dto.ReceiptsResponse
import io.nearpay.terminalsdk.data.dto.ReconciliationListResponse
import io.nearpay.terminalsdk.data.dto.ReconciliationReceiptsResponse
import io.nearpay.terminalsdk.data.dto.ReconciliationResponse
import io.nearpay.terminalsdk.data.dto.RefundTransaction
import io.nearpay.terminalsdk.data.dto.SendTransaction

import io.nearpay.terminalsdk.data.dto.TransactionResponse
import io.nearpay.terminalsdk.data.dto.TransactionsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

const val TRANSACTION_REFRESH_ENDPOINT = "v1/refresh"
const val SEND_TRANSACTION_ENDPOINT = "terminals/v1/transactions/"

interface TransactionServiceApi : HealthCheckServiceApi {


    @POST("terminals/v1/transactions/")
    suspend fun sendTransaction(
        @Body sendTransactionBody: SendTransaction
    ): TransactionResponse

    @POST("terminals/v1/transactions/refund")
    suspend fun refundTransaction(
        @Body refundTransactionBody: RefundTransaction
    ): TransactionResponse

    @POST("terminals/v1/transactions/cencel")
    suspend fun cancelTransaction(
        @Body cancelTransactionBody: ID
    ): IsCanceled

    @POST("terminals/v1/reconciliations")
    suspend fun reconcile(): ReconciliationReceiptsResponse

    @GET("terminals/v1/transactions/")
    suspend fun getTransactions(
        @Query("page") page: String? = null,
        @Query("limit") limit: String? = null,
        @Query("is_reconciled") isReconciled: String? = null,
        @Query("date") date: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null,
        @Query("customer_reference_number") customerReferenceNumber: String? = null
    ): TransactionsResponse

    @GET("terminals/v1/transactions/{transactionUuid}")
    suspend fun getTransaction(
        @Path("transactionUuid") transactionUuid: String
    ): ReceiptsResponse

    @GET("terminals/v1/reconciliations")
    suspend fun getReconciliations(
        @Query("page") page: String? = null,
        @Query("limit") limit: String? = null,
        @Query("from") from: String? = null,
        @Query("to") to: String? = null
    ): ReconciliationListResponse


    @GET("terminals/v1/reconciliations/{uuid}")
    suspend fun getReconciliation(
        @Path("uuid") uuid: String
    ): ReconciliationResponse





//
//    @POST("v1/reverse")
//    suspend fun postReversal(
//        @Body uuid: Reversal,
//        @Header(CONNECT_TIMEOUT_HEADER) connectTimeoutMillis: String,
//        @Header(READ_TIMEOUT_HEADER) readTimeoutMillis: String,
//        @Header(WRITE_TIMEOUT_HEADER) writeTimeoutMillis: String
//    ): List<TransactionReceipt>
//
//    @POST("v1/reconcile")
//    suspend fun reconcile(@Header("uuid") reconcileUUID: String): Reconciliation
//
//    @POST(TRANSACTION_REFRESH_ENDPOINT)
//    suspend fun refreshToken(@Query("hash") hash: String): Auth
//
//    @POST("v1/transactionLookup/{rrn}")
//    suspend fun postTransactionLookup(@Path("rrn") transactionRRN: String): TransactionLookupResponse
//
//    @GET("v1/pendingTransactions/total")
//    suspend fun getPendingTransactionsTotal(): PendingTransactionTotal
//
//    /**
//     * is_reconciled = undefined -> getAllTransactions
//     * is_reconciled = false -> getPendingTransactions
//     * is_reconciled = true -> getReconciledTransactions
//     */
//    @GET("v1/listTransactions")
//    suspend fun getTransactionsList(
//        @Query("page") page: Int,
//        @Query("limit") limit: Int,
//        @Query("is_reconciled") isReconciled: Boolean? = null,
//        @Query("from") startDate: String? = null,
//        @Query("to") endDate: String? = null,
//        @Query("date") date: String? = null,
//        @Query("customer_reference_number") customerReferenceNumber: String? = null
//    ): TransactionBannerList
//
//    @GET("v1/transaction/{transactionUuid}")
//    suspend fun getTransaction(@Path("transactionUuid") terminalUuid: String): List<TransactionReceipt>
//
//    @GET("v1/reconciliations")
//    suspend fun getReconcile(
//        @Query("page") page: Int,
//        @Query("limit") limit: Int,
//        @Query("from") startDate: String? = null,
//        @Query("to") endDate: String? = null
//    ): ReconciliationList
//
//    @GET("v1/reconciliations/{transactionUuid}")
//    suspend fun getReconcile(@Path("transactionUuid") transactionUuid: String): Reconciliation
//
//    @GET("v1/report")
//    suspend fun getReport(@Query("page") page: Int, @Query("limit") limit: Int): ReportList
//
//    @POST("v1/confirmation")
//    suspend fun sendConfirmation(@Body confirmation: Confirmation)
//
//    @GET("v1/sessions/{id}")
//    suspend fun postSession(@Path("id") id: String): SessionResponse
//
//    @POST("v1/kernel_log")
//    suspend fun kernelLog(@Body kernelLog: EncryptedData)
}