package com.maths.apiapplication.signup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.signup.model.SignupResponse
import com.maths.apiapplication.signup.repository.SignupRepository
import com.maths.apiapplication.base.NetworkResult

class SignupViewModel : ViewModel() {

    private var signUpRepository = SignupRepository()

  suspend  fun signUp(email: String, name: String, password: String, confirmnewpassword : String,
               fcm_token : String) : LiveData<NetworkResult<SignupResponse?>> {
      return signUpRepository.signup(email, name, password, confirmnewpassword, fcm_token)
    }





}





// ibsar
//class SignupViewModel(private val signupRepository: SignupRepository = SignupRepository()):ViewModel(){
//
//    private val _signUpResponseLiveData = MutableLiveData<Response<SignupResponse>>()
//    val signUpResponseLiveData: MutableLiveData<Response<SignupResponse>> = _signUpResponseLiveData
//    fun signup(name: String, email: String, password: String, confirmNewPassword: String, fcmToken: String)  {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val response = signupRepository.signup(email, name, password, confirmNewPassword, fcmToken)
//                _signUpResponseLiveData.postValue(response)
//
//            }
//            catch (e : HttpException){
//                Log.e("err" , e.toString())
//            }
//
//        }
//    }
//
//}