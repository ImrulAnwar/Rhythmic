package com.example.rhythmic.ui.activities.now_playing_activity

import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.rhythmic.*
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.databinding.ActivityNowPlayingBinding
import com.example.rhythmic.services.MusicService
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "NowPlayingActivity"

@AndroidEntryPoint
class NowPlayingActivity : AppCompatActivity(), ServiceConnection {

        private lateinit var binding: ActivityNowPlayingBinding
        private val nowPlayingViewModel: NowPlayingViewModel by viewModels()
        private var musicService: MusicService? = null
        lateinit var currentSong: Song
        private var position: Int = 0
        private var isPlaying: Boolean = true
        private var isRepeat: Boolean? = null
        private var isShuffle: Boolean? = null
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
                position = intent.getIntExtra("position", 0)
                currentSong = nowPlayingViewModel.getSong(position)
                nowPlayingViewModel.currentSong.value = currentSong
                isPlaying = true
                isShuffle = sharedPreferences.getBoolean("isShuffle", false)
                isRepeat = sharedPreferences.getBoolean("isRepeat", false)
                setImageResource()





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

        private fun setImageResource() {
                if (isPlaying)
                        binding.ibPlayOrPause.setImageResource(R.drawable.ic_pause)
                if (isShuffle == true)
                        binding.ibShuffle.setColorFilter(resources.getColor(R.color.black))
                else
                        binding.ibShuffle.setColorFilter(resources.getColor(R.color.text_color_2))
                if (isRepeat == true)
                        binding.ibRepeat.setImageResource(R.drawable.ic_repeat)
                else
                        binding.ibRepeat.setImageResource(R.drawable.ic_loop)
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
                binding.ibShuffle.setOnClickListener { button ->
                        if (isShuffle == true) {
                                binding.ibShuffle.setColorFilter(resources.getColor(R.color.text_color_2))
                                isShuffle = false
                                addToSharedPref("isShuffle", isShuffle == true)
                        } else {
                                binding.ibShuffle.setColorFilter(resources.getColor(R.color.black))
                                isShuffle = true
                                addToSharedPref("isShuffle", isShuffle == true)
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
                        nowPlayingViewModel.playOrPause(it)
                }
        }


        override fun onServiceDisconnected(name: ComponentName?) {
                musicService = null
        }

        fun playNextSong() {
                position++
                currentSong = nowPlayingViewModel.getSong(position)
                nowPlayingViewModel.currentSong.value = currentSong
        }

        fun addToSharedPref(key: String, value: Boolean) {
                val editor = sharedPreferences.edit()
                editor.apply {
                        putBoolean(key, value)
                        apply()
                }
        }


}