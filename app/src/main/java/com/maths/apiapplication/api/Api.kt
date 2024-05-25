package com.maths.apiapplication.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface Api {



    @FormUrlEncoded
    @POST("signup")
   suspend  fun signup(
        @Field("email") email:String ,
        @Field("name") name:String ,
        @Field("password") password: String,
        @Field("confirmnewpassword") confirmNewPassword :String,
        @Field("fcm_token") fcmToken : String
    ) : Response<JsonObject>



    @GET("get_profile")
    fun getProfile(@Query("id") id :String) : Call<JsonObject>

    @Multipart
@POST("profile_update")
fun updateProfile(
        @Part("id") id: RequestBody,
        @Part("name") name: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("address") address: RequestBody,
        @Part profile_img: MultipartBody.Part?
                  ) : Call<JsonObject>



    @GET("privacy_policy")
    fun privacyPolicy(): Call<JsonObject>

    @GET("terms_condition")
    fun termsCondition(): Call<JsonObject>
@GET("about_us")
fun about(): Call<JsonObject>


}