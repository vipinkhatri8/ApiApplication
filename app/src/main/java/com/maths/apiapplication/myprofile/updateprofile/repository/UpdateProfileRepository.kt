package com.maths.apiapplication.myprofile.updateprofile.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maths.apiapplication.myprofile.updateprofile.models.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateProfileRepository {
    var apiService = RetrofitClass.apiInterface
    fun updateProfile(id : RequestBody, name: RequestBody, email: RequestBody, phone: RequestBody, password: RequestBody, file: MultipartBody.Part?): LiveData<NetworkResult<UpdateProfileResponse?>> {
//        val idReq = id.toRequestBody("text/plain".toMediaTypeOrNull())
//        val nameReq = name.toRequestBody("text/plain".toMediaTypeOrNull())
//        val emailReq = email.toRequestBody("text/plain".toMediaTypeOrNull())
//        val phoneReq = phone.toRequestBody("text/plain".toMediaTypeOrNull())
//        val passwordReq = password.toRequestBody("text/plain".toMediaTypeOrNull())
        var updateProfileLiveData = MutableLiveData<NetworkResult<UpdateProfileResponse?>>()
        apiService.updateProfile(id, name, email, phone, password, file).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
              try {
                  if (response.isSuccessful) {
                      var jsonObject = response.body()
                      if (jsonObject != null) {
                          var gson = Gson()
                          var updateProfileResponse =
                              gson.fromJson(jsonObject, UpdateProfileResponse::class.java)

                          var success = updateProfileResponse.success
                          var code  = updateProfileResponse.code

                          if (success && code == 200) {
                              updateProfileLiveData.postValue(
                                  NetworkResult.Success(
                                      updateProfileResponse
                                  )
                              )
                          } else {
                              updateProfileLiveData.postValue(NetworkResult.Error("${updateProfileResponse.code} ${updateProfileResponse.message}"))

                          }


                      } else {
                          updateProfileLiveData.postValue(NetworkResult.Error("Server error"))
                      }
                  }
              }

             catch (e: Exception) {
                  updateProfileLiveData.postValue(NetworkResult.Error("${e.message}"))
              }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                updateProfileLiveData.postValue(NetworkResult.Error("${t.message}"))
                t.printStackTrace()
            }


        })

        return  updateProfileLiveData
    }
}