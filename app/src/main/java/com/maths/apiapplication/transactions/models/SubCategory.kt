package com.maths.apiapplication.transactions.models

import com.google.gson.annotations.SerializedName

data class SubCategory(@SerializedName("SubCategory_name") val subCategoryName: String,
                       @SerializedName("id") val id: Int)
