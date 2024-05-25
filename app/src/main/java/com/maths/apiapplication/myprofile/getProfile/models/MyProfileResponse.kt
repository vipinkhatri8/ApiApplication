package com.maths.apiapplication.myprofile.getProfile.models

import com.maths.apiapplication.myprofile.getProfile.models.MyProfileDataResponse


data class MyProfileResponse(var  success: Boolean,
                             var  code: Int,
                             var  message: String,
                             var  data: MyProfileDataResponse
)
