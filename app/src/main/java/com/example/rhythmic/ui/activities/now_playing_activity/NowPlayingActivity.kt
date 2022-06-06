package com.example.rhythmic.ui.activities.now_playing_activity

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bumptech.glide.Glide
import com.example.rhythmic.*
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.ActivityNowPlayingBinding
import com.example.rhythmic.recievers.NotificationReceiver
import com.example.rhythmic.services.MusicService
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "NowPlayingActivity"

@AndroidEntryPoint
class NowPlayingActivity : AppCompatActivity(), ServiceConnection {

        private lateinit var binding: ActivityNowPlayingBinding
        private val nowPlayingViewModel: NowPlayingViewModel by viewModels()
        private var musicService: MusicService? = null
        lateinit var currentSong: Song
        private var postion: Int = 0
        private var isPlaying: Boolean = true


        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNowPlayingBinding.inflate(layoutInflater)
                setContentView(binding.root)
                supportActionBar?.hide()
                setButtonActions()
                postion = intent.getIntExtra("position", 0)
                currentSong = nowPlayingViewModel.getSong(postion)
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
                        nowPlayingViewModel.getBitmapAndShowNotification(it, this)
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