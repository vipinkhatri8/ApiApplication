package com.maths.apiapplication.myprofile.getProfile.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maths.apiapplication.myprofile.getProfile.models.MyProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyProfileRepository {
    var apiService = RetrofitClass.apiInterface

    fun getProfile(id: String) : LiveData<NetworkResult<MyProfileResponse?>>{
        var getProfileLiveData = MutableLiveData<NetworkResult<MyProfileResponse?>>()
        apiService.getProfile(id).enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
             try {
                 if (response.isSuccessful) {
                     var jsonObject = response.body()
                     if (jsonObject != null) {
                         var gson = Gson()
                         var getProfileResponse =
                             gson.fromJson(jsonObject, MyProfileResponse::class.java)
                         var success = getProfileResponse.success
                         var code = getProfileResponse.code
                         if (success && code == 200) {
                             getProfileLiveData.postValue(NetworkResult.Success(getProfileResponse))
                         } else {
                             getProfileLiveData.postValue(NetworkResult.Error("${getProfileResponse.code} ${getProfileResponse.message}"))
                         }

                     } else {


                         // Handle the case where response.isSuccessful is false
                         getProfileLiveData.postValue(NetworkResult.Error("Server error"))
                     }
                 }
             }
              catch (e: Exception) {
                 // Handle any other exceptions that may occur
                 getProfileLiveData.postValue(NetworkResult.Error(" ${e.message}"))
             }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // Handle the failure case of the network call
                getProfileLiveData.postValue(NetworkResult.Error("${t.message}"))
                t.printStackTrace()
            }

        })

        return getProfileLiveData
    }
}