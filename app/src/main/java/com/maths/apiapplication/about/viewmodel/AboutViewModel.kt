package com.maths.apiapplication.about.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.about.models.AboutRespone
import com.maths.apiapplication.about.repository.AboutRepository

class AboutViewModel :ViewModel(){
    var aboutRepository = AboutRepository()
    fun about() : LiveData<NetworkResult<AboutRespone?>>{
        return  aboutRepository.aboutUs()

    }
}