package com.example.rhythmic.ui.activities.now_playing_activity

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rhythmic.*
import com.example.rhythmic.databinding.ActivityNowPlayingBinding
import com.example.rhythmic.services.MusicService
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "NowPlayingActivity"

@AndroidEntryPoint
class NowPlayingActivity : AppCompatActivity(), ServiceConnection {

        private lateinit var binding: ActivityNowPlayingBinding
        private val nowPlayingViewModel: NowPlayingViewModel by viewModels()
        private var musicService: MusicService? = null
        private val sharedPreferences: SharedPreferences by lazy {
                getSharedPreferences(
                        "sharedPref",
                        Context.MODE_PRIVATE
                )
        }


        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = ActivityNowPlayingBinding.inflate(layoutInflater)
                setContentView(binding.root)
                supportActionBar?.hide()
                setButtonActions()
                nowPlayingViewModel.currentPosition = intent.getIntExtra("position", 0)
                nowPlayingViewModel.setCurrentSong(nowPlayingViewModel.getSong(nowPlayingViewModel.currentPosition))
                nowPlayingViewModel.setIsPlaying(true)
                nowPlayingViewModel.isShuffle = sharedPreferences.getBoolean("isShuffle", false)
                nowPlayingViewModel.isRepeat = sharedPreferences.getBoolean("isRepeat", false)



                nowPlayingViewModel.getCurrentSong().observe(this) {
                        Toast.makeText(this, "songChanged", Toast.LENGTH_SHORT).show()

                        Glide.with(this).load(it.imagePath)
                                .placeholder(R.mipmap.ic_launcher).centerCrop()
                                .into(binding.ivAlbumArtNP)

                        val duration = nowPlayingViewModel.convertTime(it.duration?.toLong() ?: 0)
                        binding.tvTotalDurationNP.text = duration
                        binding.tvSongTitleNP.text = it.title
                        binding.tvArtistNameNP.text = it.artist
                        if(it.isLiked) binding.ivIsLikedNP.setImageResource(R.drawable.ic_loved)
                        else binding.ivIsLikedNP.setImageResource(R.drawable.ic_love)
                }

                nowPlayingViewModel.isPlaying().observe(this) {
                        if (it) binding.ibPlayOrPause.setImageResource(R.drawable.ic_pause)
                        else binding.ibPlayOrPause.setImageResource(R.drawable.ic_play)
                        //could be more organized
                        val playPauseButton: Int = if (nowPlayingViewModel.isPlaying().value ==true) R.drawable.ic_pause else R.drawable.ic_play
                        nowPlayingViewModel.getCurrentSong().value?.let {song->
                                val likeButton: Int = if (song.isLiked) R.drawable.ic_loved else R.drawable.ic_love
                                nowPlayingViewModel.getBitmapAndShowNotification(song, this, intent, playPauseButton= playPauseButton, likeButton = likeButton)
                        }
                }

        }

        private fun setImageResource() {
                if (nowPlayingViewModel.isShuffle == true)
                        binding.ibShuffle.setColorFilter(resources.getColor(R.color.black))
                else
                        binding.ibShuffle.setColorFilter(resources.getColor(R.color.text_color_2))
                if (nowPlayingViewModel.isRepeat == true)
                        binding.ibRepeat.setImageResource(R.drawable.ic_repeat)
                else
                        binding.ibRepeat.setImageResource(R.drawable.ic_loop)
        }

        private fun setButtonActions() {
                binding.ibPlayOrPause.setOnClickListener {
                        var isPlaying: Boolean
                        binding.ibPlayOrPause.apply {
                                isPlaying = if (nowPlayingViewModel.isPlaying().value == true) {
                                        musicService?.pause()
                                        false
                                } else {
                                        musicService?.resume()
                                        true
                                }
                        }
                        nowPlayingViewModel.setIsPlaying(isPlaying)
                }
                binding.ibShuffle.setOnClickListener {
                        if (nowPlayingViewModel.isShuffle == true) {
                                binding.ibShuffle.setColorFilter(resources.getColor(R.color.text_color_2))
                                nowPlayingViewModel.isShuffle = false
                                addToSharedPref("isShuffle", nowPlayingViewModel.isShuffle == true)
                        } else {
                                binding.ibShuffle.setColorFilter(resources.getColor(R.color.black))
                                nowPlayingViewModel.isShuffle = true
                                addToSharedPref("isShuffle", nowPlayingViewModel.isShuffle == true)
                        }
                }
                binding.ibRepeat.setOnClickListener {
                        if (nowPlayingViewModel.isRepeat == true) {
                                binding.ibRepeat.setImageResource(R.drawable.ic_loop)
                                nowPlayingViewModel.isRepeat = false
                                addToSharedPref("isRepeat", nowPlayingViewModel.isRepeat == true)
                        } else {
                                binding.ibRepeat.setImageResource(R.drawable.ic_repeat)
                                nowPlayingViewModel.isRepeat = true
                                addToSharedPref("isRepeat", nowPlayingViewModel.isRepeat == true)
                        }
                }
                binding.ibNext.setOnClickListener {
                        musicService?.let {
                                nowPlayingViewModel.playNextSong(it)
                                nowPlayingViewModel.setIsPlaying(true)
                                binding.ibPlayOrPause.setImageResource(R.drawable.ic_pause)
                        }
                        if (nowPlayingViewModel.isRepeat == true)
                                Toast.makeText(this, "Player is on Repeat mode", Toast.LENGTH_SHORT)
                                        .show()
                }
                binding.ibPrev.setOnClickListener {
                        musicService?.let {
                                nowPlayingViewModel.playPrevSong(it)
                                nowPlayingViewModel.setIsPlaying(true)
                                binding.ibPlayOrPause.setImageResource(R.drawable.ic_pause)
                        }
                        if (nowPlayingViewModel.isRepeat == true)
                                Toast.makeText(this, "Player is on Repeat mode", Toast.LENGTH_SHORT)
                                        .show()
                }
        }

        override fun onResume() {
                Intent(this, MusicService::class.java).also {
                        bindService(it, this, BIND_AUTO_CREATE)
                }
                setImageResource()
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
                        it.setViewModel(this)
                        nowPlayingViewModel.startMedia(it)
                }
        }


        override fun onServiceDisconnected(name: ComponentName?) {
                musicService = null
        }

        private fun addToSharedPref(key: String, value: Boolean) {
                val editor = sharedPreferences.edit()
                editor.apply {
                        putBoolean(key, value)
                        apply()
                }
        }

}