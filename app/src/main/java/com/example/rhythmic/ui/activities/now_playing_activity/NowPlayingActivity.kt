package com.example.rhythmic.ui.activities.now_playing_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rhythmic.R

class NowPlayingActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.activity_now_playing)
                supportActionBar?.hide()
        }
}