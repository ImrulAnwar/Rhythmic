package com.example.rhythmic.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.rhythmic.*
import com.example.rhythmic.ui.activities.now_playing_activity.NowPlayingViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MusicService"

@AndroidEntryPoint
class MusicService : Service(), MediaPlayer.OnCompletionListener {
        companion object {
                var currentSongPath: String = ""
                private lateinit var nowPlayingViewModel: NowPlayingViewModel
        }

        @Inject lateinit var mediaPlayer: MediaPlayer

        private val binder: IBinder by lazy { MusicBinder() }


        fun setViewModel( context: ViewModelStoreOwner) {
                nowPlayingViewModel = ViewModelProvider(context)[NowPlayingViewModel::class.java]
        }

        override fun onBind(intent: Intent?): IBinder {
                return binder
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                val actionName: String = intent?.getStringExtra("actionName")!!
                actionName.let {
                        when (it) {
                                ACTION_PLAY -> {
                                        if (isPlaying()) pause()
                                        else resume()
                                        showNotification(intent)
                                }
                                ACTION_NEXT -> {
                                        nowPlayingViewModel.playNextSong(this)
                                        showNotification(intent)
                                }
                                ACTION_PREV -> {
                                        nowPlayingViewModel.playPrevSong(this)
                                        showNotification(intent)
                                }
                                ACTION_LIKE -> {
                                        Toast.makeText(this, "like", Toast.LENGTH_SHORT)
                                                .show()
                                        showNotification(intent)
                                }
                                else -> {}
                        }
                }
                return START_NOT_STICKY
        }

        private fun showNotification(intent: Intent) {
                val playPauseButton: Int = if (nowPlayingViewModel.isPlaying().value ==true) R.drawable.ic_pause else R.drawable.ic_play
                nowPlayingViewModel.getCurrentSong().value?.let {song->
                        val likeButton: Int = if (song.isLiked) R.drawable.ic_loved else R.drawable.ic_love
                        nowPlayingViewModel.getBitmapAndShowNotification(song, this, intent, playPauseButton= playPauseButton, likeButton = likeButton)
                }
        }

        fun seekTo(progress: Int) {
                mediaPlayer.seekTo(progress)
        }

        fun getDuration()= mediaPlayer.duration

        inner class MusicBinder : Binder() {
                fun getService(): MusicService {
                        return this@MusicService
                }
        }

        fun startMedia(path: String?) {
                nowPlayingViewModel.setIsPlaying(true)
                path?.let {
                        mediaPlayer.setDataSource(this, Uri.parse(it))
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        currentSongPath = it
                        mediaPlayer.setOnCompletionListener(this)
                }
        }

        fun changeDataSource(path: String?) {
                path?.let {
                        mediaPlayer.reset()
                        startMedia(path)
                }
        }

        fun pause() {
                nowPlayingViewModel.setIsPlaying(false)
                mediaPlayer.pause()
        }

        fun resume() {
                nowPlayingViewModel.setIsPlaying(true)
                mediaPlayer.start()
        }

        fun reset() {
                mediaPlayer.reset()
        }

        fun isPlaying(): Boolean = mediaPlayer.isPlaying
        fun isNotPlaying(): Boolean = !mediaPlayer.isPlaying
        fun isNotAlreadyPlaying(path: String?): Boolean = path != currentSongPath
        fun isNotPaused(): Boolean = !(!mediaPlayer.isPlaying && mediaPlayer.currentPosition > 1)
        override fun onCompletion(p0: MediaPlayer?) {
                nowPlayingViewModel.playNextSong(this@MusicService)
        }


}