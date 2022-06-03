package com.example.rhythmic.ui.activities.now_playing_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rhythmic.R
import com.example.rhythmic.databinding.ActivityMainBinding
import com.example.rhythmic.databinding.ActivityNowPlayingBinding

class NowPlayingActivity : AppCompatActivity() {

        private lateinit var binding: ActivityNowPlayingBinding

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNowPlayingBinding.inflate(layoutInflater)
                setContentView(binding.root)
                supportActionBar?.hide()
        }
}