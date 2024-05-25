package com.maths.apiapplication.transactions.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.maths.apiapplication.transactions.models.TransactionsResponse
import com.maths.apiapplication.transactions.repository.TransactionsRepository

class TransactionsViewModel : ViewModel(){

    var transactionsRepository = TransactionsRepository()
    fun fetchTransactions(id: String) : LiveData<NetworkResult<TransactionsResponse?>> {
        return  transactionsRepository.fetchTransactions(id)
    }
}