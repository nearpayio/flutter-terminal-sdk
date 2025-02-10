package io.nearpay.terminalsdk

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import io.nearpay.softpos.data.dto.PaymentCardScheme
import io.nearpay.softpos.library.LinkDeviceCallBack
import io.nearpay.softpos.library.PaymentText
import io.nearpay.softpos.library.ProvisionCallBack
import io.nearpay.terminalsdk.data.dto.ReaderEnvelope as TerminalSDKReaderEnvelope
import io.nearpay.softpos.library.ReaderJobCallBack
import io.nearpay.softpos.library.ReaderJobState
import io.nearpay.softpos.library.TransactionType
import io.nearpay.softpos.library.TransactionUiMessage
import io.nearpay.softpos.reader_ui.ReadCardUIParamsBuilder
import io.nearpay.softpos.reader_ui.ReaderCoreUI
import io.nearpay.softpos.utils.Environment
import io.nearpay.softpos.utils.NearPayError
import io.nearpay.terminalsdk.data.dto.Country
import io.nearpay.terminalsdk.data.dto.Location
import io.nearpay.terminalsdk.data.dto.NetworkParams
import io.nearpay.terminalsdk.data.dto.PaymentScheme
import io.nearpay.terminalsdk.data.dto.ReaderEnvelope
import io.nearpay.terminalsdk.data.local.TerminalSharedPreferences
import io.nearpay.terminalsdk.data.mapProperties
import io.nearpay.terminalsdk.data.remote.ServerSwitcher
import io.nearpay.terminalsdk.data.remote.TransactionNetwork
import io.nearpay.terminalsdk.data.remote.TransactionServiceApi
import io.nearpay.terminalsdk.data.usecases.CancelTransactionUseCase
import io.nearpay.terminalsdk.data.usecases.GetReconciliationListUseCase
import io.nearpay.terminalsdk.data.usecases.GetReconciliationUseCase
import io.nearpay.terminalsdk.data.usecases.GetTransactionUseCase
import io.nearpay.terminalsdk.data.usecases.GetTransactionsListUseCase
import io.nearpay.terminalsdk.data.usecases.ReconcileUseCase
import io.nearpay.terminalsdk.data.usecases.RefundTransactionUseCase
import io.nearpay.terminalsdk.data.usecases.SendTransactionUseCase
import io.nearpay.terminalsdk.listeners.CancelTransactionListener
import io.nearpay.terminalsdk.listeners.GetReconciliationListListener
import io.nearpay.terminalsdk.listeners.GetReconciliationListener
import io.nearpay.terminalsdk.listeners.GetTransactionListener
import io.nearpay.terminalsdk.listeners.GetTransactionsListListener
import io.nearpay.terminalsdk.listeners.ReadCardListener
import io.nearpay.terminalsdk.listeners.ReconcileListener
import io.nearpay.terminalsdk.listeners.RefundTransactionListener
import io.nearpay.terminalsdk.listeners.SendTransactionListener
import io.nearpay.terminalsdk.listeners.failures.GetTransactionsListFailure
import io.nearpay.terminalsdk.listeners.failures.ReconcileFailure
import io.nearpay.terminalsdk.utils.NetworkClientConfig
import io.nearpay.terminalsdk.utils.getAppVersion
import io.nearpay.terminalsdk.utils.runOnMainThread
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class Terminal(private val networkParams: NetworkParams,
               private val terminalSharedPreferences: TerminalSharedPreferences,
               private val activity: Activity,
               private val tid: String,
//               private val userUUID: String,
               private val readerCoreUI: ReaderCoreUI)
{


    private lateinit var readerEnvelope: TerminalSDKReaderEnvelope
    private lateinit var mTerminalId: String
    private var isReady: Boolean = false
    private var activeReaderCore: ReaderCoreUI
    private val scope = CoroutineScope(Dispatchers.IO)
    private var serverSwitcher: ServerSwitcher<TransactionServiceApi>
    private val initializationDeferred = CompletableDeferred<Unit>()

    companion object {
        private val terminalStates = mutableMapOf<String, TerminalState>()
         var counter = 0
    }
    private fun provideSendTransactionUseCase(): SendTransactionUseCase {
        return SendTransactionUseCase(
            serverSwitcher = serverSwitcher,
        )
    }
    private fun provideRefundTransactionUseCase(): RefundTransactionUseCase {
        return RefundTransactionUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    private fun provideCancelTransactionUseCase(): CancelTransactionUseCase {
        return CancelTransactionUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    private fun provideReconcileUseCase(): ReconcileUseCase {
        return ReconcileUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    private fun provideGetTransactionsListUseCase(): GetTransactionsListUseCase {
        return GetTransactionsListUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    private fun provideGetTransactionUseCase(): GetTransactionUseCase {
        return GetTransactionUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    private fun provideGetReconciliationUseCase(): GetReconciliationUseCase {
        return GetReconciliationUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    private fun provideGetReconciliationListUseCase(): GetReconciliationListUseCase {
        return GetReconciliationListUseCase(
            serverSwitcher = serverSwitcher,
        )
    }

    init {
//        if terminal is not in the user list of terminals, throw an exception
//        terminalSharedPreferences.getTerminalsListForUser(userUUID).firstOrNull { it.tid == tid } ?: throw IllegalArgumentException("Terminal not found in user's list of terminals")
//        if(terminalSharedPreferences.getTerminalsListForUser(userUUID).firstOrNull { it.tid == tid } == null){
//            Timber.tag("Terminal").d("Terminal not found in user's list of terminals")
//            throw IllegalArgumentException("Terminal not found in user's list of terminals")
//        }
//        else {
//            Timber.tag("Terminal").d("Terminal found in user's list of terminals")
//        }


        val transactionNetwork = TransactionNetwork(
            context = activity,
            sharedPreferences = terminalSharedPreferences,
            packageName = activity.packageName,
            appsVersions =  mapOf("App-Version" to getAppVersion(activity)),
            tid = tid
        )
        val networkConfigs =
            when (networkParams.country) {
                Country.SA -> when (networkParams.environment) {
                    Environment.SANDBOX -> Pair(
                        NetworkClientConfig.SANDBOX_MTLS_1,
                        NetworkClientConfig.SANDBOX_MTLS_2
                    )

                    Environment.PRODUCTION -> Pair(
                        NetworkClientConfig.PRODUCTION_MTLS_1,
                        NetworkClientConfig.PRODUCTION_MTLS_2
                    )
                }

                Country.TR -> when (networkParams.environment) {
                    Environment.SANDBOX -> Pair(
                        NetworkClientConfig.TR_SANDBOX_MTLS_1,
                        NetworkClientConfig.TR_SANDBOX_MTLS_1
                    )

                    Environment.PRODUCTION -> Pair(
                        NetworkClientConfig.TR_PRODUCTION_MTLS_1,
                        NetworkClientConfig.TR_PRODUCTION_MTLS_1
                    )
                }

                Country.USA -> Pair(
                    NetworkClientConfig.SANDBOX_MTLS_2,
                    NetworkClientConfig.SANDBOX_MTLS_2

                )
            }

        serverSwitcher =
            ServerSwitcher(
                transactionNetwork.getTransactionServiceApi(networkConfigs.first),
                transactionNetwork.getTransactionServiceApi(networkConfigs.second)
            )

        transactionNetwork.setServerSwitcher(serverSwitcher)

        //cdbc55f3-7d8b-47b3-b8c2-385505d88363
        //5a9c499b-8cf2-42aa-8bd3-a9a89626dc14
        Timber.tag("Terminal").d("Terminal created with tid: $tid")
        Timber.d(terminalStates.values.toString())

        if (terminalStates[tid] == null) {
            Timber.tag("Terminal").d("Terminal not found in cache. Provisioning and linking device.")
            activeReaderCore = readerCoreUI

            provisionAndLinkDevice()
        }
        else {
            Timber.tag("Terminal").d("Terminal found in cache. Checking if terminal is provisioned and linked.")
            val terminalState = terminalStates[tid]!!
            isReady = terminalState.isProvisioned && terminalState.isLinked
            activeReaderCore = terminalState.readerCore
            mTerminalId = terminalState.terminalId
            Timber.d("The terminal is provisioned and linked" +terminalStates.values.toString())
            initializationDeferred.complete(Unit)
        }
    }

    private data class TerminalState(
        val readerCore: ReaderCoreUI,
        var isProvisioned: Boolean,
        var isLinked: Boolean,
        var terminalId : String
    )

// TODO: fun authorization
// TODO: fun disconnect terminal
private fun provisionAndLinkDevice() {
    activeReaderCore.provision(object : ProvisionCallBack {
        override fun onFailure(error: NearPayError) {
            Timber.tag("Provisioning").d("Provisioning failed: $error")
            isReady = false
            initializationDeferred.completeExceptionally(throw IllegalArgumentException(error.message))
        }

        override fun onSuccess(deviceToken: String) {
            Timber.tag("Provisioning").d("Provisioning successful")
            linkDevice()
        }
    })
//    linkDevice()
}

    private fun linkDevice() {
        val linkDeviceCallBack = object : LinkDeviceCallBack {
            override fun onFailure(error: NearPayError) {
                Timber.tag("LinkDevice").d("Linking failed: $error")
                isReady = false
                initializationDeferred.completeExceptionally(throw IllegalArgumentException(error.message))
            }

            override fun onSuccess(terminalId: String) {
                mTerminalId = terminalId
                terminalStates[tid] = TerminalState(activeReaderCore, isProvisioned = true, isLinked = true, terminalId = terminalId)
                Timber.d(terminalStates.keys.toString())
                Timber.d(terminalStates.values.toString())
                Timber.tag("LinkDevice").d("Linking successful: $terminalId")
                isReady = true
                initializationDeferred.complete(Unit)
            }
        }

        val readerToken = terminalSharedPreferences.getReaderToken(tid)
        activeReaderCore.linkDevice(readerToken, linkDeviceCallBack)
    }

    fun isTerminalReady(): Boolean {
        return isReady
    }


    fun purchase(amount: Long, scheme: PaymentScheme? = null,
                 readCardListener: ReadCardListener, sendTransactionListener: SendTransactionListener) {
        scope.launch {
            try {
                // Wait for initialization to complete
                initializationDeferred.await()
                if (!isReady) {
                    Timber.tag("Purchase")
                        .d("Terminal is not ready. Ensure provisioning and linking are successful.")
                    readCardListener.onReaderError("Terminal not ready")
                    return@launch
                }

                performCardRead(
                    amount,
                    TransactionType.PURCHASE,
                    scheme,
                    readCardListener,
                    sendTransactionListener
                )

            } catch (e: Exception) {
                Timber.tag("Purchase").d(e)
                readCardListener.onReaderError(e.message)
                return@launch
            }
        }
    }

    fun refund( amount: Long,
                refundRRN: String,
                scheme: PaymentScheme?,
                readCardListener: ReadCardListener,
                refundTransactionListener: RefundTransactionListener) {
        if (!isReady) {
            Timber.tag("Refund").d("Terminal is not ready. Ensure provisioning and linking are successful.")
            readCardListener.onReaderError("Terminal not ready")
            return
        }
        performCardRead(amount,TransactionType.REFUND, scheme, readCardListener,
            refundTransactionListener = refundTransactionListener,
            refundRRN = refundRRN)
    }

    private fun performCardRead(amount: Long, transactionType: TransactionType,
                                scheme: PaymentScheme?,
                                readCardListener: ReadCardListener,
                                sendTransactionListener: SendTransactionListener? = null,
                                refundTransactionListener: RefundTransactionListener? = null,
                                refundRRN: String? = null,
    ) {
        val currencyNumericCode = when(networkParams.country){
            Country.SA -> 682
            Country.TR -> 949
            Country.USA -> 840
        }
        val readerJobCallBack: ReaderJobCallBack =
            object : ReaderJobCallBack {
                override fun onReaderError(error: NearPayError) {
                    runOnMainThread{
                        readCardListener.onReaderError(error.message)
                    }
                    Timber.e("onReaderError $error")
                }

                override fun onReaderStateChanged(state: ReaderJobState) {
                    Timber.d("onReaderStateChanged " + state.asJsonString())
                    when (state) {
                        is ReaderJobState.JobStarted -> {
                            Timber.d("Job started and reader is ready")
                            runOnMainThread {
                                readCardListener.onReadingStarted()
                            }
                        }

                        is ReaderJobState.Reader.Waiting -> {
                            Timber.d("Reader Waiting$state")
                            runOnMainThread{
                                readCardListener.onReaderWaiting()
                            }
                        }

                        is ReaderJobState.Reader.Reading -> {
                            Timber.d("Reader Reading$state")
                            runOnMainThread{
                                readCardListener.onReaderReading()
                            }
                        }

                        is ReaderJobState.Pin.Entering -> {
                            Timber.d("Reader Entering$state")
                            runOnMainThread{
                                readCardListener.onPinEntering()
                            }
                        }

                        is ReaderJobState.Reader.Retry -> {
                            Timber.d("Reader Retry$state")
                            runOnMainThread{
                                readCardListener.onReaderRetry()
                            }
                        }

                        is ReaderJobState.Reader.Finished -> {
                            Timber.d("Reader Finished$state")
                            runOnMainThread{
                                readCardListener.onReaderFinished()
                            }
                        }

                        is ReaderJobState.Reader.ReaderResult -> {
                            // Reading finished successfully and returned a result
                            readerEnvelope = mapProperties(state.value)

                            Timber.d("im in reader result with counter ${counter++}")
                            Timber.d("Reader Result triggered - Transaction Type: $transactionType")
                            Timber.d("Reader Envelope: $readerEnvelope")
                            Timber.d("Stack trace:", Exception()) // This will show you where it's being called from
                            runOnMainThread{
                                readCardListener.onReadCardSuccess()
                            }

                            if (transactionType == TransactionType.PURCHASE) {
                                if (sendTransactionListener != null) {
                                    sendTransaction(readerEnvelope, Location(0.0, 0.0), sendTransactionListener)
                                }
                            }
                            else if (transactionType == TransactionType.REFUND) {
                                Timber.d("Refund RRN: $refundRRN")
                                Timber.d("amount: $amount")
                                    refundTransaction(readerEnvelope, Location(0.0, 0.0), refundRRN!!, refundTransactionListener!!)
                            }
                            else{
                                Timber.tag("handleReadCard").d("Transaction type not supported")
                                throw IllegalArgumentException("Transaction type not supported")
                            }



                            //                            DONE

//                            getTransaction("ced10c7c-d6e5-4522-a5d6-99c02f85bd0a", object : GetTransactionListener {
//                                override fun onGetTransactionSuccess(transaction: ReceiptsResponse) {
//                                    Timber.tag("handleReadCard").d("Transaction: $transaction")
//                                }
//
//                                override fun onGetTransactionFailure(error: GetTransactionFailure) {
//                                    Timber.tag("handleReadCard").d("GetTransaction failed $error")
//                                }
//                            })


                            //                            DONE
//                            reconcile( object : ReconcileListener {
//                                override fun onReconcileSuccess(reconciliationReceiptsResponse : ReconciliationReceiptsResponse) {
//                                    Timber.tag("handleReadCard").d("Reconcile: $reconciliationReceiptsResponse")
//                                }
//
//                                override fun onReconcileFailure(reconcileFailure: ReconcileFailure) {
//                                    Timber.tag("handleReadCard").d("Reconcile failed $reconcileFailure")
//                                }
//                            })




                            //                            DONE
//                            getTransactionsList(
//                                1, 10, null, object : GetTransactionsListListener {
//                                    override fun onGetTransactionsListSuccess(transactionsList: TransactionsResponse) {
//                                        Timber.tag("handleReadCard")
//                                            .d("Transactions list: $transactionsList")
//                                    }
//
//                                    override fun onGetTransactionsListFailure(error: GetTransactionsListFailure) {
//                                        Timber.tag("handleReadCard")
//                                            .d("GetTransactionsList failed $error")
//                                    }
//                                },
//                                startDate = TODO(),
//                                endDate = TODO(),
//                                customerReferenceNumber = TODO(),
//                                getTransactionsListListener = TODO()
//                            )
//
//                            getReconciliationList(1, 10, null, null, object : GetReconciliationListListener {
//                                override fun onGetReconciliationListSuccess(reconciliationListResponse: ReconciliationListResponse) {
//                                    Timber.tag("handleReadCard").d("Reconciliation list: $reconciliationListResponse")
//                                }
//
//                                override fun onGetReconciliationListFailure(error: GetReconciliationListFailure) {
//                                    Timber.tag("handleReadCard").d("GetReconciliationList failed $error")
//                                }
//                            })


//                            DONE
//                            getReconciliation("ced10c7c-d6e5-4522-a5d6-99c02f85bd0a", object : GetReconciliationListener {
//                                override fun onGetReconciliationSuccess(reconciliationReceiptsResponse : ReconciliationReceiptsResponse) {
//                                    Timber.tag("handleReadCard").d("Reconciliation: $reconciliationReceiptsResponse")
//                                }
//
//                                override fun onGetReconciliationFailure(error: GetReconciliationFailure) {
//                                    Timber.tag("handleReadCard").d("GetReconciliation failed $error")
//                                }
//                            })




//                            refundTransaction(readerEnvelope, Location(0.0, 0.0), "501898000000", object : RefundTransactionListener {
//                                override fun onRefundTransactionSuccess(transactionResponse: TransactionResponse) {
//                                    Timber.tag("handleReadCard").d("Refund: $transactionResponse")
//                                }
//
//                                override fun onRefundTransactionFailure(refundTransactionFailure: RefundTransactionFailure) {
//                                    Timber.tag("handleReadCard").d("Refund failed $refundTransactionFailure")
//                                }
//                            })


                        }

                        is ReaderJobState.Reader.Error -> {
                            Timber.tag("handleReadCard").d("ReadCard failed ${state.niceError}")
                            runOnMainThread{
                                readCardListener.onReaderError(state.niceError.message)
                            }
                        }
                    }
                }

                override fun onPaymentNetwork(state: PaymentCardScheme?) {
                }

                override fun onTransactionUiMessage(state: TransactionUiMessage?) {
                }
            }

        val readCardParams = ReadCardUIParamsBuilder()
            .profileId(mTerminalId)
            .amount(amount) // Example: 1000 = 100 Riyals
            .transactionType(transactionType) // ReaderTransactionType.PURCHASE or ReaderTransactionType.REFUND
            .currencyNumericCode(currencyNumericCode) // Example: 682 for SAR
            .currencyDefaultFractionDigits(2) // Example: 2
//            .apply {
//                scheme?.let { this.scheme(it.getScheme()) }
//            }
            .pinPosition(Gravity.BOTTOM)
            .paymentText(PaymentText("ادفع", "Tap to pay"))
            .activity(activity)
            .readerJobCallBack(readerJobCallBack)
            .build()

        activeReaderCore.readCard(readCardParams)
    }

    private fun sendTransaction(readerEnvelope: ReaderEnvelope, location: Location, sendTransactionListener: SendTransactionListener) {
        scope.launch {
            provideSendTransactionUseCase()
                .invoke(readerEnvelope, location, sendTransactionListener)
        }
    }

     private fun refundTransaction(readerEnvelope: ReaderEnvelope, location: Location, refundRRN: String ,refundTransactionListener: RefundTransactionListener) {
        scope.launch {
            provideRefundTransactionUseCase()
                .invoke(readerEnvelope, location, refundRRN, refundTransactionListener)
        }
    }

//    private fun performPinRead() {
//        val readPinCallBack = object : PinJobCallBack {
//            override fun onPinStateChanged(state: PinJobState) {
//                Timber.d("onPinStateChanged $state")
//                when (state) {
//                    PinJobState.JobStarted -> {}
//
//                    is PinJobState.Pin.Entering -> {
//                    }
//
//                    is PinJobState.Pin.Result -> {
//                    }
//
//                    is PinJobState.Pin.Error -> {
//                    }
//
//                    PinJobState.JobEnded ->  {
//
//                    }
//                }
//            }
//
//            override fun onReaderError(error: NearPayError) {
//                Timber.e("onReaderError $error")
//            }
//
//            override fun onTransactionUiMessage(state: TransactionUiMessage?) {
//            }
//        }
//
//        val readPinParams = ReadPinParamsBuilder()
//            .profileId(mTerminalId)
//            .pinJobCallBack(readPinCallBack)
//            .activity(activity)
//            .pinPosition(Gravity.BOTTOM)
//            .build()
//
//        activeReaderCore.readPin(readPinParams)
//    }

    fun cancelTransaction(id: String, cancelTransactionListener: CancelTransactionListener) {
        scope.launch {
            provideCancelTransactionUseCase()
                .invoke(id, cancelTransactionListener)
        }
    }

    fun reconcile(reconcileListener: ReconcileListener) {
        scope.launch {
            provideReconcileUseCase()
                .invoke(reconcileListener)
        }
    }


     fun getTransactionsList(page: Int? = null, pageSize: Int? = null,
                               isReconciled: Boolean? = null, date: Long? = null, startDate: Long? = null,
                             endDate: Long? = null, customerReferenceNumber: String? = null, getTransactionsListListener: GetTransactionsListListener)
     {
        scope.launch {
            provideGetTransactionsListUseCase()
                .invoke(page = page, pageSize = pageSize, isReconciled = isReconciled, date = date, startDate = startDate,
                    endDate = endDate, customerReferenceNumber = customerReferenceNumber, getTransactionsListListener = getTransactionsListListener)
        }
    }

    fun getTransaction(uuid: String, getTransactionListener: GetTransactionListener) {
        scope.launch {
            provideGetTransactionUseCase()
                .invoke(
                    uuid,
                    getTransactionListener = getTransactionListener
                )
        }
    }

    fun getReconciliation(uuid: String, getReconciliationListener : GetReconciliationListener) {
        scope.launch {
            provideGetReconciliationUseCase()
                .invoke(
                    uuid,
                    getReconciliationListener = getReconciliationListener
                )
        }
    }

    fun getReconciliationList(page: Int? = null, pageSize: Int? = null , startDate: Long?= null, endDate: Long? = null, getReconciliationListListener: GetReconciliationListListener) {
        scope.launch {
            provideGetReconciliationListUseCase()
                .invoke(
                    page = page,
                    pageSize = pageSize,
                    startDate = startDate,
                    endDate = endDate,
                    getReconciliationListListener = getReconciliationListListener
                )
        }
    }

}