package com.maths.apiapplication.privacypolicy.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maths.apiapplication.privacypolicy.models.PrivacyPolicyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/*
  Html.fromHtml(
                                    Html.fromHtml(
                                        termAndConditionResponse.data.description
                                    ).toString()
                                )
                            )
 */


class PrivacyPolicyRepository {
    var apiService = RetrofitClass.apiInterface
    fun privacyPolicy(): LiveData<NetworkResult<PrivacyPolicyResponse?>> {
        var privacyPolicyLiveData = MutableLiveData<NetworkResult<PrivacyPolicyResponse?>>()
        apiService.privacyPolicy().enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                try {
                    if (response.isSuccessful) {
                        var jsonObject = response.body()
                        if (jsonObject != null) {
                            var gson = Gson()
                            var privacyPolicyResponse =
                                gson.fromJson(jsonObject, PrivacyPolicyResponse::class.java)
                            var code = privacyPolicyResponse.code
                            var success = privacyPolicyResponse.success

                                if (success &&  code == 200) {
                                    privacyPolicyLiveData.postValue(
                                        NetworkResult.Success(
                                            privacyPolicyResponse
                                        )
                                    )
                                }

                             else {
                                privacyPolicyLiveData.postValue(NetworkResult.Error("${privacyPolicyResponse.code} ${privacyPolicyResponse.message}"))
                            }


                        } else {
                            privacyPolicyLiveData.postValue(NetworkResult.Error("Server error"))
                        }

                    }
                } catch (e: Exception) {
                    // Handle any other exceptions that may occur
                    privacyPolicyLiveData.postValue(NetworkResult.Error("${e.message}"))
                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                privacyPolicyLiveData.postValue(NetworkResult.Error(" ${t.message}"))
                t.printStackTrace()
            }


        })






        return privacyPolicyLiveData
    }
}