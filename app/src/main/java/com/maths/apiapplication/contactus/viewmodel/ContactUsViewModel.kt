package com.maths.apiapplication.contactus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.contactus.models.ContactUsResponse
import com.maths.apiapplication.contactus.repository.ContactUsRepository

class ContactUsViewModel : ViewModel(){
    var contactUsRepository = ContactUsRepository()
    fun contactUs(user_name: String, phone: String, email: String, subject: String, messages: String, user_type: String) : LiveData<NetworkResult<ContactUsResponse?>>{
      return  contactUsRepository.contactUs(user_name, phone, email, subject, messages, user_type)
    }
}