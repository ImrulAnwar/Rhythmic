package com.example.rhythmic.ui.activities.now_playing_activity

import android.app.*
import android.content.*
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.rhythmic.*
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository
import com.example.rhythmic.receivers.NotificationReceiver
import com.example.rhythmic.services.MusicService
import com.example.rhythmic.ui.activities.main_activity.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject


private const val TAG = "NowPlayingActivityVM"

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {
        //        var currentSong = MutableLiveData<Song>()
//        var currentPosition: Int = 0
        val currentSongListSize by lazy { repository.getCurrentSongLIst().size }
        private lateinit var mediaSessionCompat: MediaSessionCompat

        var seekPosition = MutableLiveData(0.toLong())
        fun getCurrentSong() = repository.getCurrentSong()
        fun setCurrentSong(song: Song) {
                repository.setCurrentSong(song)
        }

        fun setProgress(int: Long) {
                seekPosition.value = int
        }

        fun getProgress(): LiveData<Long> = seekPosition

        fun setSeekBarProgress() {
                viewModelScope.launch(Dispatchers.IO) {
                        var long: Long? = seekPosition.value
                        long?.let {
                                long++
                        }
                        setProgress(long ?: 0)
                        delay(1000)
                }
        }

        fun isPlaying(): LiveData<Boolean> = repository.isPlaying()
        private val sharedPreferences: SharedPreferences by lazy {
                application.applicationContext.getSharedPreferences(
                        "sharedPref",
                        Context.MODE_PRIVATE
                )
        }
        var isRepeat: Boolean? = null

        var isShuffle: Boolean? = null

        fun setIsPlaying(boolean: Boolean) {
                repository.setIsPlaying(boolean)
        }

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

        fun getBitmapAndShowNotification(
                currentSong: Song, context: Context, playPauseButton: Int,
                likeButton: Int
        ) {
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_love)
                        .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                ) {
                                        showNotification(
                                                currentSong,
                                                resource,
                                                context,
                                                playPauseButton = playPauseButton,
                                                likeButton = likeButton
                                        )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                        })

                Glide.with(context)
                        .asBitmap()
                        .load(currentSong.imagePath)
                        .into(object : CustomTarget<Bitmap>() {
                                override fun onResourceReady(
                                        resource: Bitmap,
                                        transition: Transition<in Bitmap>?
                                ) {
                                        showNotification(
                                                currentSong,
                                                resource,
                                                context,
                                                playPauseButton = playPauseButton,
                                                likeButton = likeButton
                                        )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                        })
        }


        private fun setPosition() {
                if (getCurrentSongPosition() >= currentSongListSize) {
                        setCurrentSongPosition(0)
                } else if (getCurrentSongPosition() < 0) {
                        setCurrentSongPosition(currentSongListSize - 1)
                }
        }

        fun playNextSong(musicService: MusicService) {
                if (isRepeat == false)
                        setCurrentSongPosition(getCurrentSongPosition() + 1)
                setPosition()
                playNextOrPrevSong(musicService, getCurrentSongPosition())
        }

        fun playPrevSong(musicService: MusicService) {
                if (isRepeat == false)
                        setCurrentSongPosition(getCurrentSongPosition() - 1)
                setPosition()
                playNextOrPrevSong(musicService, getCurrentSongPosition())
        }


        private fun showNotification(
                currentSong: Song,
                bitmap: Bitmap,
                context: Context,
                playPauseButton: Int,
                likeButton: Int
        ) {

                val intent: Intent = Intent(context, MainActivity::class.java)
                val pendingIntent: PendingIntent = PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        0
                )


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

                val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(bitmap)
                        .setContentTitle(currentSong.title)
                        .setContentText(currentSong.artist)
                        .setContentIntent(pendingIntent)
                        .addAction(R.drawable.ic_prev, "Previous", prevPendingIntent)
                        .addAction(playPauseButton, "Pause", pausePendingIntent)
                        .addAction(R.drawable.ic_next, "Next", nextPendingIntent)
                        .addAction(likeButton, "Like", likePendingIntent)
                        .setOnlyAlertOnce(true)
                        .setOngoing(true)
                        .setStyle(
                                androidx.media.app.NotificationCompat.MediaStyle()
                                        .setMediaSession(mediaSessionCompat.sessionToken)
                        )
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .build()
                notification.flags = Notification.FLAG_AUTO_CANCEL


                val notificationManager =
                        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.notify(1, notification)

        }

        fun startMedia(musicService: MusicService) {
                if (repository.getCurrentSongLIst().isNotEmpty())
                        musicService.let {
                                //if song is not playing it could be paused
                                // if it is paused & it was already playing will just resume it
                                // else reset the player and start music
                                if (it.isNotPlaying()) {
                                        if (it.isNotPaused()) {
                                                it.startMedia(getCurrentSong().value?.path)
                                        } else {
                                                if (!it.isNotAlreadyPlaying(getCurrentSong().value?.path)) {
                                                        it.resume()
                                                } else {
                                                        it.reset()
                                                        it.startMedia(getCurrentSong().value?.path)
                                                }
                                        }
                                } else if (it.isNotAlreadyPlaying(getCurrentSong().value?.path)) {
                                        it.changeDataSource(getCurrentSong().value?.path)
                                }
                        }
        }

        fun postCurrentSong(song: Song) {
                repository.postCurrentSong(song)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun addToLiked() {
                GlobalScope.launch(Dispatchers.IO) {
                        getCurrentSong().value?.let {
                                it.isLiked = !(it.isLiked)
                                repository.updateSong(it)
                                postCurrentSong(it)
                        }
                }
        }

        @DelicateCoroutinesApi
        fun addToLikedAndChangeNotificationIcon(context: Context, intent: Intent) {
                GlobalScope.launch(Dispatchers.IO) {
                        getCurrentSong().value?.let {
                                it.isLiked = !(it.isLiked)
                                repository.updateSong(it)
                                postCurrentSong(it)
                        }
                        changeNotificationIcons(context, intent)
                }
        }

        fun changeNotificationIcons(context: Context, intent: Intent) {
                val playPauseButton: Int =
                        if (isPlaying().value == true) R.drawable.ic_pause else R.drawable.ic_play
                getCurrentSong().value?.let { song ->
                        val likeButton: Int =
                                if (song.isLiked) R.drawable.ic_loved else R.drawable.ic_love
                        getBitmapAndShowNotification(
                                song,
                                context,
                                playPauseButton = playPauseButton,
                                likeButton = likeButton
                        )
                }
        }

        fun changeNotificationIconsWithoutIntent(context: Context) {
                val playPauseButton: Int =
                        if (isPlaying().value == true) R.drawable.ic_pause else R.drawable.ic_play
                getCurrentSong().value?.let { song ->
                        val likeButton: Int =
                                if (song.isLiked) R.drawable.ic_loved else R.drawable.ic_love
                        getBitmapAndShowNotification(
                                song,
                                context,
                                playPauseButton = playPauseButton,
                                likeButton = likeButton
                        )
                }
        }


        fun getCurrentSongPosition(): Int = repository.getCurrentSongPosition()
        fun setCurrentSongPosition(int: Int) {
                repository.setCurrentSongPosition(int)
        }

        private fun playNextOrPrevSong(musicService: MusicService, currentPosition: Int) {
                setProgress(0)
                isRepeat = sharedPreferences.getBoolean("isRepeat", false)
                isShuffle = sharedPreferences.getBoolean("isShuffle", false)
                if (isRepeat == false)
                        if (isShuffle == true)
                                setCurrentSongPosition(Random().nextInt(currentSongListSize))
                        else
                                setCurrentSongPosition(currentPosition)
                repository.setCurrentSong(repository.getCurrentSongLIst()[getCurrentSongPosition()])
                musicService.changeDataSource(repository.getCurrentSong().value?.path)
        }
}