package com.maths.apiapplication.myprofile.updateprofile.models


data class UpdateProfileResponse(var  success: Boolean,
                                 var  code: Int,
                                 var  message: String,
                                 var  data: UpdateProfileDataResponse
)
