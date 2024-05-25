package com.maths.apiapplication.transactions.models

import com.google.gson.annotations.SerializedName

data class Tech(@SerializedName("name") val name: String,
                      @SerializedName("id") val id: Int)
