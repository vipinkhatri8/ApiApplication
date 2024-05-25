package com.maths.apiapplication.signup.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.maths.apiapplication.signup.model.SignupResponse

import com.maths.apiapplication.api.RetrofitClient
import com.maths.apiapplication.base.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SignupRepository{
    private val apiService = RetrofitClient.apiInterface

    suspend fun signup( email: String, name: String, password: String, confirmnewpassword: String,
                       fcm_token: String): LiveData<NetworkResult<SignupResponse?>> {
        val userLiveData = MutableLiveData<NetworkResult<SignupResponse?>>()
        userLiveData.postValue(NetworkResult.Loading())

        try {
            val response = withContext(Dispatchers.IO) {
                apiService.signup(email, name, password, confirmnewpassword, fcm_token)
            }
            if (response.isSuccessful) {
                val jsonObject = response.body()
                if (jsonObject != null) {
                    val gson = Gson()
                    val signupResponse = gson.fromJson(jsonObject, SignupResponse::class.java)

                    val success = signupResponse.success
                    val code = signupResponse.code
                    Log.d("LoginSuccess", success.toString())

                    if (success && code == 200) {
                        userLiveData.postValue(NetworkResult.Success(signupResponse))
                    } else {
                        userLiveData.postValue(NetworkResult.Error("${signupResponse.message} ${signupResponse.code}"))
                    }
                } else {
                    userLiveData.postValue(NetworkResult.Error("Server error"))
                }
            } else {
                userLiveData.postValue(NetworkResult.Error(response.message()))
            }
        } catch (e: Exception) {
            userLiveData.postValue(NetworkResult.Error("${e.message}"))
        }

        return userLiveData
    }
}






//ibsar
//class SignupRepository(private val apiService : ApiService =  ApiService()){
//
//    val apiservice =  RetrofitClient.apiInterface
//
//
//    suspend fun signup( email: String ,name : String, password:String , confirmNewPassword:String, fcmToken:String) : Response<SignupResponse> {
//        //return  apiService.signup(email, name, password, confirmNewPassword, fcmToken)
//        try {
//            return signUP(email , name , password , confirmNewPassword, fcmToken)
//        }
//        catch (e: HttpException) {
//            // Handle HTTP exceptions
//            throw Exception("Failed to sign up: ${e.message}")
//        }
//
//
//    }
//    suspend fun signUP(email: String, name : String, password:String, confirmNewPassword:String, fcmToken:String) : Response<SignupResponse>{
//        return apiService.apiInterface.signup(email , name , password , confirmNewPassword, fcmToken)
//    }
//
//}