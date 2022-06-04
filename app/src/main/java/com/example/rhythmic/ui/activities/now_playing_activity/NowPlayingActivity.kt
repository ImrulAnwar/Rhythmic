package com.example.rhythmic.ui.activities.now_playing_activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.rhythmic.R
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.ActivityNowPlayingBinding
import com.example.rhythmic.domain.MediaPlayerFunctions
import com.example.rhythmic.services.MusicService

private const val TAG = "NowPlayingActivity"

class NowPlayingActivity : AppCompatActivity(), ServiceConnection {

        private lateinit var binding: ActivityNowPlayingBinding
        private val nowPlayingViewModel: NowPlayingViewModel by viewModels()
        private var musicService: MusicService? = null
        lateinit var currentSong: Song
        private var isPlaying: Boolean = true

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNowPlayingBinding.inflate(layoutInflater)
                setContentView(binding.root)
                supportActionBar?.hide()
                setButtonActions()
                currentSong = intent.getSerializableExtra("currentSong") as Song
                nowPlayingViewModel.currentSong.value = currentSong
                isPlaying = true


                nowPlayingViewModel.currentSong.observe(this) {
                        Glide.with(this).load(it.imagePath)
                                .placeholder(R.mipmap.ic_launcher).centerCrop()
                                .into(binding.ivAlbumArtNP)

                        val duration = nowPlayingViewModel.convertTime(it.duration?.toLong() ?: 0)
                        binding.tvTotalDurationNP.text = duration
                        binding.tvSongTitleNP.text = it.title
                        binding.tvArtistNameNP.text = it.artist
                }
        }

        private fun setButtonActions() {
                binding.ibPlayOrPause.setOnClickListener {
                        binding.ibPlayOrPause.apply {
                                isPlaying = if (isPlaying) {
                                        musicService?.pause()
                                        setColorFilter(resources.getColor(R.color.white))
                                        setImageResource(R.drawable.ic_play)
                                        false
                                } else {
                                        musicService?.resume()
                                        setColorFilter(resources.getColor(R.color.white))
                                        setImageResource(R.drawable.ic_pause)
                                        true
                                }
                        }
                }
        }

        override fun onResume() {
                Intent(this, MusicService::class.java).also {
                        bindService(it, this, BIND_AUTO_CREATE)
                }

                super.onResume()
        }

        override fun onPause() {
                unbindService(this)
                super.onPause()
        }

        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
                val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
                musicService = binder.getService()
                musicService?.let {
                        //if song is not playing it could be paused
                        // if it is paused & it was already playing will just resume it
                        // else reset the player and start music
                        if (it.isNotPlaying()) {
                                if (it.isNotPaused()) {
                                        it.startMedia(currentSong.path)
                                } else {
                                        if (!it.isNotAlreadyPlaying(currentSong.path)) {
                                                it.resume()
                                        } else {
                                                it.reset()
                                                it.startMedia(currentSong.path)
                                        }
                                }
                        } else if (it.isNotAlreadyPlaying(currentSong.path)) {
                                it.changeDataSource(currentSong.path)
                        }
                }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
                musicService = null
        }
}