package com.example.nikecore.ui.onBoarding

import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.android.synthetic.main.onboarding_fragment.*
import kotlinx.coroutines.*

class OnboardingViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    var i : MutableLiveData<Int> = MutableLiveData(0)
    init {
        viewModelScope.launch {
            while(true) {
                delay(5000)
                i.value = (i.value!! + 1) % 3
            }



        }
    }
}