package com.maths.apiapplication.privacypolicy.models

import com.maths.apiapplication.privacypolicy.models.PrivacyPolicyDataResponse

data class PrivacyPolicyResponse(var  success: Boolean,
                                 var  code: Int,
                                 var  message: String,
                                 var  data: PrivacyPolicyDataResponse
)
