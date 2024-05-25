package com.maths.apiapplication.contactus.models

import com.google.gson.annotations.SerializedName

data class ContactUsDataResponse(
    @SerializedName("user_name")  var user_name : String,
    @SerializedName("phone")   var phone : String,
    @SerializedName("email")   var email : String,
    @SerializedName("subject")   var subject : String,
    @SerializedName("message")   var messageData : String,
    @SerializedName("user_type")   var user_type : String,
    @SerializedName("updated_at")    var updated_at : String,
    @SerializedName("created_at") var created_at : String,
    @SerializedName("id") var id : Int

)
