package com.maths.apiapplication.myprofile.updateprofile.models

data class UpdateProfileDataResponse(
    var id : Int,
    var name : String,
    var email : String,
    var phone : String,
    var profile_img : Any,
    var login_type : Int


)
