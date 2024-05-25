package com.maths.apiapplication.about.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maths.apiapplication.about.models.AboutRespone
import com.maths.apiapplication.api.RetrofitClient
import com.maths.apiapplication.base.NetworkResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutRepository {
    var apiService = RetrofitClient.apiInterface

fun aboutUs() : LiveData<NetworkResult<AboutRespone?>>{
    var aboutLiveData = MutableLiveData<NetworkResult<AboutRespone?>>()
    apiService.aboutUs().enqueue(object : Callback<JsonObject>{
        override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
        //    try {
                if (response.isSuccessful) {
                    var jsonObject = response.body()
                    if (jsonObject != null) {
                        var gson = Gson()
                        var aboutRespone  = gson.fromJson(jsonObject, AboutRespone::class.java)
                        var code = aboutRespone.code
                        var success = aboutRespone.success
                        if (code == 200 && success) {
                            aboutLiveData.postValue(
                                NetworkResult.Success(
                                    aboutRespone
                                )
                            )
                        }
                        else {
                            aboutLiveData.postValue(NetworkResult.Error(" ${aboutRespone.message} ${aboutRespone.code}"))
                        }
                    } else {
                        aboutLiveData.postValue(NetworkResult.Error("Server error"))
                    }
                }
//            } catch (e: Exception) {
//                // Handle any other exceptions that may occur
//                aboutLiveData.postValue(NetworkResult.Error(" ${e.message}"))
//            }
        }
        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
            aboutLiveData.postValue(NetworkResult.Error("${t.message}"))
            t.printStackTrace()
        }


    })

    return  aboutLiveData
}





}