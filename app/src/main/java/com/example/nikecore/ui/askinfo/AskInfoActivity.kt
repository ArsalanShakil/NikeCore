package com.example.nikecore.ui.askinfo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.nikecore.MainActivity
import com.example.nikecore.R
import kotlinx.android.synthetic.main.activity_ask_info.*

class AskInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_info)

        getStartedAskBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}