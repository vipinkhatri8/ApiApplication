package com.maths.apiapplication.contactus.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maths.apiapplication.contactus.models.ContactUsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsRepository {
    var apiservice = RetrofitClass.apiInterface

    fun contactUs(user_name: String, phone: String, email: String, subject: String, messages: String, user_type: String)
    : LiveData<NetworkResult<ContactUsResponse?>> {
        var contactLiveData = MutableLiveData<NetworkResult<ContactUsResponse?>>()
        apiservice.contactUs(user_name, phone, email, subject, messages, user_type).enqueue(object  : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (response.isSuccessful) {
                        var jsonObject = response.body()
                        if (jsonObject != null) {
                            var gson = Gson()
                            var contactUsResponse1  = gson.fromJson(jsonObject, ContactUsResponse::class.java)
                            var code = contactUsResponse1.code
                            var success = contactUsResponse1.success
                            if (code == 200 && success) {
                                contactLiveData.postValue(
                                        NetworkResult.Success(
                                            contactUsResponse1
                                        )
                                    )
                                }
                            else {
                                contactLiveData.postValue(NetworkResult.Error(" ${contactUsResponse1.message} ${contactUsResponse1.code}"))
                            }
                        } else {
                            contactLiveData.postValue(NetworkResult.Error("Server error"))
                        }
                    }
                } catch (e: Exception) {
                    // Handle any other exceptions that may occur
                    contactLiveData.postValue(NetworkResult.Error(" ${e.message}"))
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                contactLiveData.postValue(NetworkResult.Error("${t.message}"))
                t.printStackTrace()
            }
        })
        return contactLiveData
    }

}