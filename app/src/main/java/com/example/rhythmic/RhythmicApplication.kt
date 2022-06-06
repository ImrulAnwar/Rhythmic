package com.example.rhythmic

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
const val CHANNEL_ID = "channel_1"
const val ACTION_LIKE = "action_like"
const val ACTION_NEXT = "action_next"
const val ACTION_PREV = "action_prev"
const val ACTION_PLAY = "action_play"

@HiltAndroidApp
class RhythmicApplication: Application(){

        override fun onCreate() {
                super.onCreate()
                createNotificationChannel()
        }

        private fun createNotificationChannel() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                                CHANNEL_ID,
                                CHANNEL_ID,
                                NotificationManager.IMPORTANCE_HIGH
                        )
                        channel.description = "music service"
                        val notificationManager = getSystemService(
                                NotificationManager::class.java
                        )
                        notificationManager.createNotificationChannel(channel)
                }
        }
}