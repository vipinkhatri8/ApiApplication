package com.maths.apiapplication.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging

import com.maths.apiapplication.signup.viewmodel.SignupViewModel
import com.maths.apiapplication.base.NetworkResult
import com.maths.apiapplication.commonutils.CommonUtils

import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignupBinding

    val signupViewModel: SignupViewModel by lazy {
        ViewModelProvider(this).get(SignupViewModel::class.java)
    }
lateinit var  commonUtils: CommonUtils

    lateinit var name: String
    lateinit var password: String
    lateinit var email: String
    lateinit var confirmPassword: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
commonUtils = CommonUtils()
        initView()
        getDeviceToken()
        FirebaseApp.initializeApp(this)

    }

    private fun initView() {


        binding!!.rlSignup.setOnClickListener {
            name = binding.etFullName?.text.toString()
            email = binding?.etEmail?.text.toString()
            password = binding?.etPass?.text.toString()
            confirmPassword = binding?.etConfirmPass?.text.toString()
            //viewModel.signup(name , email , password , confirmPassword , fcmToken)

            if (validate()) {
                if (commonUtils.internetCheck(this)) {
                    signUpApi(email, name, password, confirmPassword)
                }
            }
        }


    }

    private fun validate(): Boolean {
        val emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]"
        val emapattern = Pattern.compile(emailpattern)
        val emailmatcher = emapattern.matcher(binding!!.etEmail.text.toString().trim())

        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\S+\$).{6,}\$"
        val pattern = Pattern.compile(passwordPattern)
        val passmatcger = pattern.matcher(binding!!.etPass.text.toString().trim())

        if (binding!!.etFullName.text.toString().trim().isEmpty()) {
            binding!!.etFullName.error = "Please Enter Full Name."
            binding!!.etFullName.requestFocus()
            return false
        } else if (binding!!.etEmail.text.toString().trim().isEmpty()) {
            binding!!.etEmail.error = "Please Enter Email/Phone"
            binding!!.etEmail.requestFocus()
            return false
        } else if (!emailmatcher.find() && !validNumber()) {
            binding!!.etEmail.error = "Please Enter Valid Email or  phone number"
            binding!!.etEmail.requestFocus()
            return false
        } else if (binding!!.etPass.text.toString().isEmpty()) {
            binding!!.etPass.error = "Please Enter Password"
            binding!!.etPass.requestFocus()
            return false
        } else if (!passmatcger.find()) {
            binding!!.etPass.error =
                "Password must be a minimum of 6 characters with an at least 1 number, 1 Uppercase & special character."
            binding!!.etPass.requestFocus()
            return false
        } else if (!binding!!.etPass.text.toString().trim()
                .equals(binding!!.etConfirmPass.text.toString().trim())
        ) {
            binding!!.etPass.error =
                "Password and confirm password should be same."
            binding!!.etPass.requestFocus()
            return false;
        }

        return true
    }

    private fun validNumber(): Boolean {

        var email: String = binding!!.etEmail.text.toString().trim()
        if (email.length != 10) {
            return false;
        }
        var onlyDigits = true
        for (i in 0 until email.length) {
            if (!Character.isDigit(email.get(i))) {
                onlyDigits = false
                break
            }
        }
        return onlyDigits
    }





    private fun signUpApi(
        email: String,
        name: String,
        password: String,
        confirmPassword: String
    ) {
        signupViewModel.viewModelScope.launch {
            signupViewModel.signUp(email, name, password, confirmPassword, getDeviceToken())
                .observe(this@SignupActivity, Observer { result ->
                    when (result) {
                        is NetworkResult.Success -> {
                            result.data?.let { loginResponse ->
                                val id = loginResponse.data?.id ?: 0
                                val bundle = Bundle()
                                bundle.putInt("idprofile", id)

                                Toast.makeText(
                                    this@SignupActivity,
                                    "Sign up successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val otpVerify = loginResponse.data.otp
                                val emailVerify = loginResponse.data.email
                                val intent =
                                    Intent(this@SignupActivity, VerificationActivity::class.java)
                                intent.putExtra("emailVerify", emailVerify)
                                intent.putExtra("nameverify", name)
                                intent.putExtra("passwordVerify", password)
                                intent.putExtra("fcmToken", getDeviceToken())
                                intent.putExtra("type", "Signup")
                                startActivity(intent)
                            }
                        }

                        is NetworkResult.Error -> {
                            Toast.makeText(this@SignupActivity, result.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is NetworkResult.Loading -> {
                            // Show loading if needed
                        }
                    }
                })
        }




    }


    //This function is used for get device token of firebase
    private fun getDeviceToken(): String {
        var fcmToken = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token: String? = task.result.toString()
                if (token != null) {
                    fcmToken = token
                    // Token retrieval successful, you can use the token here
                    Log.d("FCMToken", token)
                } else {
                    Log.e("FCM Token", "Token is null")
                }
            } else {
                // Token retrieval failed, handle the error
                Log.e("FCM Token", "Fetching FCM registration token failed", task.exception)
            }
        }
        return fcmToken
    }


}

