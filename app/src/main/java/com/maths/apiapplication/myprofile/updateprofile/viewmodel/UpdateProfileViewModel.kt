package com.maths.apiapplication.myprofile.updateprofile.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.myprofile.updateprofile.models.UpdateProfileResponse
import com.maths.apiapplication.myprofile.updateprofile.repository.UpdateProfileRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateProfileViewModel : ViewModel() {
    var updateProfileRepository = UpdateProfileRepository()
    fun  updateProfile(id : RequestBody, name: RequestBody, email: RequestBody, phone: RequestBody, password: RequestBody, file: MultipartBody.Part? ) : LiveData<NetworkResult<UpdateProfileResponse?>>{


        return  updateProfileRepository.updateProfile(id, name, email, phone, password, file)
    }


}