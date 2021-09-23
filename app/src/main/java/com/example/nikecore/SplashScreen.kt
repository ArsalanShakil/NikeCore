package com.example.nikecore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent




class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this@SplashScreen, MainActivity::class.java))

        finish()

    }
}