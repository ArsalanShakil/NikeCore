package com.example.nikecore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.nikecore.ui.onBoarding.OnBoardingActivity


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashScreen,
            OnBoardingActivity::class.java))
        finish()

    }
}