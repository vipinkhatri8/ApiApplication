package com.maths.apiapplication.signup.model

data class SignupResponse(
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: SignUpData
)

data class  SignUpData(
    val id: Int,
    val email: String,
    val loginType: Int,
    val otp: String
)
