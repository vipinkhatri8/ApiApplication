package com.maths.apiapplication.transactions.models

import com.google.gson.annotations.SerializedName
import com.maths.apiapplication.transactions.models.TransactionData

data class TransactionsResponse(@SerializedName("success") val success: Boolean,
                                @SerializedName("code") val code: Int,
                                @SerializedName("message") val message: String,
                                @SerializedName("data") val data: ArrayList<TransactionData>
)
