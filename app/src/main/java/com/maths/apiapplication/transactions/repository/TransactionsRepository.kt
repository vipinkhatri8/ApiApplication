package com.maths.apiapplication.transactions.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject

import com.maths.apiapplication.transactions.models.TransactionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactionsRepository {
    var apiservice = RetrofitClass.apiInterface
    fun fetchTransactions(id : String) : LiveData<NetworkResult<TransactionsResponse?>>{
        var transactionLiveData = MutableLiveData<NetworkResult<TransactionsResponse?>>()
        apiservice.fetchTransactions(id).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (response.isSuccessful) {
                        var jsonObject = response.body()
                        if (jsonObject != null) {
                            var gson = Gson()
                            var transactionsResponse =
                                gson.fromJson(jsonObject, TransactionsResponse::class.java)
                            var code = transactionsResponse.code
                            var success = transactionsResponse.success
                            if (success && code == 200) {
                                transactionLiveData.postValue(
                                    NetworkResult.Success(
                                        transactionsResponse
                                    )
                                )


                            } else {
                                transactionLiveData.postValue(NetworkResult.Error("${transactionsResponse.code} ${transactionsResponse.message}"))
                            }


                        } else {
                            transactionLiveData.postValue(NetworkResult.Error("Server Error"))
                        }

                    }
                }
                 catch (e: Exception) {
                    // Handle any other exceptions that may occur
                    transactionLiveData.postValue(NetworkResult.Error(" ${e.message}"))
                }

            }


            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                transactionLiveData.postValue(NetworkResult.Error(" ${t.message}"))
                t.printStackTrace()



            }


        })




        return  transactionLiveData
    }
}