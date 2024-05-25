package com.maths.apiapplication.contactus.models

import com.maths.apiapplication.contactus.models.ContactUsDataResponse

data class ContactUsResponse(
    var  success: Boolean,
    var  code: Int,
    var  message: String,
    var  data: ContactUsDataResponse
)
