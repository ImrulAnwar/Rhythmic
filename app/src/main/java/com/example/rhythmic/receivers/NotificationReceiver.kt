package com.example.rhythmic.receivers

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.rhythmic.ACTION_LIKE
import com.example.rhythmic.ACTION_NEXT
import com.example.rhythmic.ACTION_PLAY
import com.example.rhythmic.ACTION_PREV
import com.example.rhythmic.services.MusicService
import com.example.rhythmic.ui.activities.now_playing_activity.NowPlayingViewModel
import com.skydoves.viewmodel.lifecycle.ViewModelLifecycleOwner

class NotificationReceiver : BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {


                val actionName: String? = intent?.action
                val serviceIntent = Intent(context, MusicService::class.java)

                actionName?.let {
                        when (it) {
                                ACTION_PLAY->serviceIntent.apply {
                                        putExtra("actionName", ACTION_PLAY)
                                        context?.startService(this)
                                }
                                ACTION_NEXT->serviceIntent.apply {
                                        putExtra("actionName", ACTION_NEXT)
                                        context?.startService(this)
                                }
                                ACTION_PREV->serviceIntent.apply {
                                        putExtra("actionName", ACTION_PREV)
                                        context?.startService(this)
                                }
                                ACTION_LIKE->serviceIntent.apply {
                                        putExtra("actionName", ACTION_LIKE)
                                        context?.startService(this)
                                }
                                else -> {}
                        }
                }
        }
}