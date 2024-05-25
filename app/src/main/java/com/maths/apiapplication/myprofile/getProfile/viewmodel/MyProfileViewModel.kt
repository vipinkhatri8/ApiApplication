package com.maths.apiapplication.myprofile.getProfile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.myprofile.getProfile.models.MyProfileResponse
import com.maths.apiapplication.myprofile.getProfile.repository.MyProfileRepository

class MyProfileViewModel : ViewModel() {
    var myProfileRepository = MyProfileRepository()
    fun getProfile(id: String) : LiveData<NetworkResult<MyProfileResponse?>>{
        return myProfileRepository.getProfile(id)
    }
}
