package com.example.nikecore.ui.onBoarding

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.VideoView
import com.example.nikecore.MainActivity
import com.example.nikecore.R
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.coroutines.*




class OnBoardingActivity : AppCompatActivity() {
    private val list =
        listOf("Use map to discover places with more coins", "Maintain your pace and earn rewards")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirstRun =
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", false)
        if (isFirstRun) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        setContentView(R.layout.activity_on_boarding)
        val videoview = findViewById<View>(R.id.videoView) as VideoView
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.nike)
        setDimension()
        videoview.setVideoURI(uri)
        videoview.start()
        videoView.setOnPreparedListener { mp -> //Start Playback
            videoView.start()
            //Loop Video
            mp!!.isLooping = true
            Log.i(TAG, "Video Started")
        }
        var audio = true
        val audioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0)
        imgBtn.setImageResource(R.drawable.ic_baseline_volume_up_24)

        imgBtn.setOnClickListener {
            if (audio) {
                audio = false
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_TOGGLE_MUTE, 0)
                imgBtn.setImageResource(R.drawable.ic_baseline_volume_off_24)

            } else {
                audio = true
                audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_UNMUTE,0)
                imgBtn.setImageResource(R.drawable.ic_baseline_volume_up_24)
            }
        }

        getStartedBtn.setOnClickListener {

            startActivity(Intent(this@OnBoardingActivity, MainActivity::class.java))
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).apply()

            finish()
        }

        GlobalScope.launch {
            var i = 0
            while (i in list.indices) {
                delay(5000)

                withContext(Dispatchers.Main) {
                    // TODO("Update UI here!")

                    showMsgTxt.text = list[i]
                    val `in`: Animation = AlphaAnimation(0.0f, 1.0f)
                    `in`.duration = 1000
                    showMsgTxt.startAnimation(`in`)
                    Log.d("TMZK", list[i])
                }
                Log.d("TMZY", i.toString())
                i++
                if (i == 2) {
                    i = 0
                }
            }
        }


    }

    private fun setDimension() {
        // Adjust the size of the video
        // so it fits on the screen
        val videoProportion = getVideoProportion()
        val screenWidth = resources.displayMetrics.widthPixels
        val screenHeight = resources.displayMetrics.heightPixels
        val screenProportion = screenHeight.toFloat() / screenWidth.toFloat()
        val lp = videoView.layoutParams
        if (videoProportion < screenProportion) {
            lp.height = screenHeight
            lp.width = (screenHeight.toFloat() / videoProportion).toInt()
        } else {
            lp.width = screenWidth
            lp.height = (screenWidth.toFloat() * videoProportion).toInt()
        }
        videoView.layoutParams = lp
    }

    // This method gets the proportion of the video that you want to display.
    // I already know this ratio since my video is hardcoded, you can get the
    // height and width of your video and appropriately generate  the proportion
    //    as :height/width
    private fun getVideoProportion(): Float {
        return 2f
    }
}