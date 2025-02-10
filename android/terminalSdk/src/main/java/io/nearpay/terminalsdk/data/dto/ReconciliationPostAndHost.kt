package io.nearpay.terminalsdk.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ReconciliationPostAndHost(

    @SerialName("schema_acquirer")
    var schema_acquirer: String? = null,

    @SerialName("schema_sponsor")
    var schema_sponsor: String? = null,

    @SerialName("debit_count")
    var debit_count: Long? = null,

    @SerialName("debit_amount")
    var debit_amount: Long? = null,

    @SerialName("credit_count")
    var credit_count: Long? = null,

    @SerialName("credit_amount")
    var credit_amount: Long? = null,

    @SerialName("cash_back_amount")
    var cash_back_amount: Long? = null,

    @SerialName("cash_advance_amount")
    var cash_advance_amount: Long? = null,

    @SerialName("authorization_count")
    var authorization_count: Long? = null
): Parcelable