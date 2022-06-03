package com.example.rhythmic.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MusicService"

@AndroidEntryPoint
class MusicService : Service() {
        @Inject lateinit var mediaPlayer: MediaPlayer
        private val binder: IBinder by lazy { MusicBinder() }
        override fun onBind(intent: Intent?): IBinder {
                Log.d(TAG, "onBind: Service Bound")
                return binder
        }
        override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
                return super.onStartCommand(intent, flags, startId)
        }

        inner class MusicBinder : Binder() {
                fun getService(): MusicService = this@MusicService
        }

}