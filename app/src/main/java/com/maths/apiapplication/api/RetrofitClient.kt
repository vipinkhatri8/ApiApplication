package com.maths.apiapplication.api

import com.maths.apiapplication.api.BaseUrl.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    var retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    var apiInterface = retrofit.create(Api::class.java)


}