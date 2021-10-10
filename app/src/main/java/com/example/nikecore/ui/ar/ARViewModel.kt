package com.example.nikecore.ui.ar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nikecore.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ARViewModel @Inject constructor(
    val mainRepository: MainRepository
) : ViewModel() {
    val isCollected : MutableLiveData<Boolean> = MutableLiveData(false)
    val userBalance : MutableLiveData<Int> = MutableLiveData(0)
    val userTicket : MutableLiveData<Int> = MutableLiveData(0)

}