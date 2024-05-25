package com.maths.apiapplication.transactions.models

import com.google.gson.annotations.SerializedName
import com.maths.apiapplication.transactions.models.Category
import com.maths.apiapplication.transactions.models.SubCategory


data class TransactionData(
    @SerializedName("id") val id: Int,
    @SerializedName("categorie_id") val categoryId: Int,
    @SerializedName("subCategorie_id") val subCategoryId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("technician_id") val technicianId: Int,
    @SerializedName("transaction_amt") val transactionAmount: String,
    @SerializedName("transaction_date") val transactionDate: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("deleted_at") val deletedAt: String?,
    @SerializedName("gettechnician") val technician: Tech,
    @SerializedName("getcategory") val category: Category,
    @SerializedName("getsubcategory") val subcategory: SubCategory


)
