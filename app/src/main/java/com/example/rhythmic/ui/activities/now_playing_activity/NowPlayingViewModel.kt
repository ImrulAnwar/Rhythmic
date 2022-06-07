package com.example.rhythmic.ui.activities.now_playing_activity

import android.app.Application
import android.app.Notification
import android.app.PendingIntent
import android.content.*
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.rhythmic.*
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository
import com.example.rhythmic.recievers.NotificationReceiver
import com.example.rhythmic.services.MusicService
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

private const val TAG = "NowPlayingActivityViewModel"

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {
        var currentSong = MutableLiveData<Song>()
        var currentPosition: Int = 0
        val currentSongListSize by lazy { repository.getCurrentSongLIst().size }
        private lateinit var mediaSessionCompat: MediaSessionCompat
        var seekPosition: Int = 0
        private val sharedPreferences: SharedPreferences by lazy {
                application.applicationContext.getSharedPreferences(
                        "sharedPref",
                        Context.MODE_PRIVATE
                )
        }
        var isRepeat: Boolean? = null
        var isShuffle: Boolean? = null

        fun convertTime(timeInt: Long): String {
                var timeInt = timeInt / 1000
                val totalTime = timeInt
                val time = StringBuilder()
                if (timeInt >= 3600) {
                        val x = timeInt / 3600
                        if (x.toString().length == 1) time.append("0")
                        time.append(x).append(":")
                        timeInt %= 3600
                }
                if (timeInt >= 60) {
                        val x = timeInt / 60
                        if (x.toString().length == 1) time.append("0").append(x)
                                .append(":") else time.append(x).append(":")
                        timeInt %= 60
                }
                if (totalTime < 60) {
                        if (timeInt.toString().length == 1) time.append("00" + ":" + "0")
                                .append(timeInt) else time.append("00" + ":").append(timeInt)
                } else {
                        if (timeInt.toString().length == 1) time.append("0")
                                .append(timeInt) else time.append(timeInt)
                }
                return time.toString()
        }

        fun getSong(position: Int): Song = repository.getCurrentSongLIst()[position]

        fun getBitmapAndShowNotification(currentSong: Song, context: Context, intent: Intent) {
                Glide.with(context)
                        .asBitmap()
                        .load(currentSong.imagePath)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                ) {
                                        showNotification(currentSong, resource, context, intent)
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                        })
        }

        private fun showNotification(
                currentSong: Song,
                bitmap: Bitmap,
                context: Context,
                intent: Intent
        ) {
                val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                val prevIntent = Intent(context, NotificationReceiver::class.java)
                        .setAction(ACTION_PREV)
                val prevPendingIntent: PendingIntent =
                        PendingIntent.getBroadcast(
                                context, 0, prevIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                val nextIntent = Intent(context, NotificationReceiver::class.java)
                        .setAction(ACTION_NEXT)
                val nextPendingIntent: PendingIntent =
                        PendingIntent.getBroadcast(
                                context, 0, nextIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                val pauseIntent = Intent(context, NotificationReceiver::class.java)
                        .setAction(ACTION_PLAY)
                val pausePendingIntent: PendingIntent =
                        PendingIntent.getBroadcast(
                                context, 0, pauseIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )

                val likeIntent = Intent(context, NotificationReceiver::class.java)
                        .setAction(ACTION_LIKE)
                val likePendingIntent: PendingIntent =
                        PendingIntent.getBroadcast(
                                context, 0, likeIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        )



                mediaSessionCompat = MediaSessionCompat(context, "Currently Playing")

//                var resource: Bitmap = bitmap
//                if (bitmap == null) {
//                        resource = BitmapFactory.decodeResource(
//                                context.getResources(),
//                                R.drawable.ic_repeat
//                        );
//                }

                val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_pause)
                        .setLargeIcon(bitmap)
                        .setContentTitle(currentSong.title)
                        .setContentText(currentSong.artist)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent)
                        .addAction(R.drawable.ic_pause, "Pause", pausePendingIntent)
                        .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
                        .addAction(R.drawable.ic_love, "Like", likePendingIntent)
                        .setOnlyAlertOnce(true)
                        .setStyle(
                                androidx.media.app.NotificationCompat.MediaStyle()
                                        .setMediaSession(mediaSessionCompat.sessionToken)
                        )
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build()

                val notificationManagerCompat =
                        NotificationManagerCompat.from(context.applicationContext)
                notificationManagerCompat.notify(1, notification)

        }

        fun startMedia(musicService: MusicService) {
                if (repository.getCurrentSongLIst().isNotEmpty())
                        musicService.let {
                                //if song is not playing it could be paused
                                // if it is paused & it was already playing will just resume it
                                // else reset the player and start music
                                if (it.isNotPlaying()) {
                                        if (it.isNotPaused()) {
                                                it.startMedia(currentSong.value?.path)
                                        } else {
                                                if (!it.isNotAlreadyPlaying(currentSong.value?.path)) {
                                                        it.resume()
                                                } else {
                                                        it.reset()
                                                        it.startMedia(currentSong.value?.path)
                                                }
                                        }
                                } else if (it.isNotAlreadyPlaying(currentSong.value?.path)) {
                                        it.changeDataSource(currentSong.value?.path)
                                }
                        }
        }

        fun playNextOrPrevSong(musicService: MusicService, currentPosition: Int) {
                isRepeat = sharedPreferences.getBoolean("isRepeat", false)
                isShuffle = sharedPreferences.getBoolean("isShuffle", false)
                if (isRepeat == false)
                        if (isShuffle == true)
                                this.currentPosition = Random().nextInt(currentSongListSize)
                        else
                                this.currentPosition = currentPosition
                currentSong.value = repository.getCurrentSongLIst()[this.currentPosition]
                musicService.changeDataSource(currentSong.value?.path)
        }
}