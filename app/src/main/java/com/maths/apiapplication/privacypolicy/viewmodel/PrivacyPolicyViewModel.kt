package com.maths.apiapplication.privacypolicy.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.privacypolicy.models.PrivacyPolicyResponse
import com.maths.apiapplication.privacypolicy.repository.PrivacyPolicyRepository

class PrivacyPolicyViewModel : ViewModel() {
    var privacyPolicyRepository = PrivacyPolicyRepository()
    fun privacyPolicy() : LiveData<NetworkResult<PrivacyPolicyResponse?>>{
      return  privacyPolicyRepository.privacyPolicy()
    }
}