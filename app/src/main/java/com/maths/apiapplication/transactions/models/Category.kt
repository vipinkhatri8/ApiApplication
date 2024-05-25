package com.maths.apiapplication.transactions.models

import com.google.gson.annotations.SerializedName

data class Category(@SerializedName("category_name") val categoryName: String,
                    @SerializedName("id") val id: Int)
