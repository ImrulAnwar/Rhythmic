package com.example.rhythmic.ui.activities.now_playing_activity

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.rhythmic.R
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.ActivityNowPlayingBinding
import com.example.rhythmic.domain.MediaPlayerFunctions
import com.example.rhythmic.services.MusicService

private const val TAG = "NowPlayingActivity"

class NowPlayingActivity : AppCompatActivity(),MediaPlayerFunctions , ServiceConnection{

        private lateinit var binding: ActivityNowPlayingBinding
        private val nowPlayingViewModel: NowPlayingViewModel by viewModels()
        private var musicService: MusicService? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNowPlayingBinding.inflate(layoutInflater)
                setContentView(binding.root)
                supportActionBar?.hide()

                nowPlayingViewModel.currentSong.value = intent.getSerializableExtra("currentSong") as Song

                nowPlayingViewModel.currentSong.observe(this){
                        Glide.with(this).load(it.imagePath)
                                .placeholder(R.mipmap.ic_launcher).centerCrop()
                                .into(binding.ivAlbumArtNP)

                        val duration = nowPlayingViewModel.convertTime(it.duration?.toLong() ?: 0)
                        binding.tvTotalDurationNP.text = duration
                        binding.tvSongTitleNP.text = it.title
                        binding.tvArtistNameNP.text = it.artist
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

        override fun playNextSong() {
                TODO("Not yet implemented")
        }

        override fun playPrevSong() {
                TODO("Not yet implemented")
        }

        override fun playOrPause() {
                TODO("Not yet implemented")
        }

        override fun addToLiked() {
                TODO("Not yet implemented")
        }

        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
                val binder : MusicService.MusicBinder = iBinder as MusicService.MusicBinder
                musicService = binder.getService()
                Toast.makeText(this, "Service Connected", Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
                musicService = null
        }
}