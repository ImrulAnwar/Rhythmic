package com.example.rhythmic.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.rhythmic.data.entities.Song
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "MusicService"

@AndroidEntryPoint
class MusicService : Service() {
        companion object {
                var currentSongPath: String = ""
        }
        @Inject
        lateinit var mediaPlayer: MediaPlayer

        private val binder: IBinder by lazy { MusicBinder() }
        override fun onBind(intent: Intent?): IBinder {
                return binder
        }

        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

                return super.onStartCommand(intent, flags, startId)
        }

        inner class MusicBinder : Binder() {
                fun getService(): MusicService = this@MusicService
        }

        fun startMedia(path: String?) {
                path?.let {
                        mediaPlayer.setDataSource(this, Uri.parse(it))
                        mediaPlayer.prepare()
                        mediaPlayer.start()
                        currentSongPath = it
                }
        }

        fun changeDataSource(path: String?) {
                path?.let {
                        mediaPlayer.reset()
                        startMedia(path)
                }
        }

        fun pause() { mediaPlayer.pause() }
        fun resume() { mediaPlayer.start() }

        fun reset(){mediaPlayer.reset()}
        fun isPlaying(): Boolean = mediaPlayer.isPlaying
        fun isNotPlaying(): Boolean = !mediaPlayer.isPlaying
        fun isNotAlreadyPlaying(path: String?): Boolean = path != currentSongPath
        fun isNotPaused(): Boolean = !(!mediaPlayer.isPlaying && mediaPlayer.currentPosition > 1)


}