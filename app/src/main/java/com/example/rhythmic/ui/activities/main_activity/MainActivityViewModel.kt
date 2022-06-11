package com.example.rhythmic.ui.activities.main_activity

import android.Manifest
import android.app.*
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.rhythmic.*
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository
import com.example.rhythmic.receivers.NotificationReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG= "MainActivityViewModel"
@HiltViewModel
class MainActivityViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {
        private lateinit var mediaSessionCompat: MediaSessionCompat
        private val requestCode: Int = 1
        fun getRuntimePermission(activity: Activity) {
                if (isPermissionDenied()
                ) {
                        ActivityCompat.requestPermissions(
                                activity,
                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                requestCode
                        )
                } else {
                        viewModelScope.launch(Dispatchers.IO) {
                                insertAllSongs(activity)
                        }
                }
        }
        fun getCurrentSong() = repository.getCurrentSong()
        fun setIsPlaying(boolean: Boolean) {
                repository.setIsPlaying(boolean)
        }
        fun isPlaying(): LiveData<Boolean> = repository.isPlaying()

        private fun isPermissionDenied() = ContextCompat.checkSelfPermission(
                getApplication<Application>().applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) != PackageManager.PERMISSION_GRANTED

        fun onRequestPermissionResult(requestCode: Int, grantResult: Int, activity: Activity) {
                if (requestCode == this.requestCode) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                                viewModelScope.launch(Dispatchers.IO) {
                                        insertAllSongs(activity)
                                }
                        } else {
                                ActivityCompat.requestPermissions(
                                        activity,
                                        arrayOf(
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                                        ),
                                        this.requestCode
                                )
                        }
                }
        }

        fun addToLiked() {
                viewModelScope.launch (Dispatchers.IO){
                        getCurrentSong().value?.let {
                                it.isLiked = !(it.isLiked)
                                repository.updateSong(it)
                                postCurrentSong(it)
                        }
                }
        }
        fun setCurrentSong(song: Song) {
                repository.setCurrentSong(song)
        }

        fun postCurrentSong(song: Song) {
                repository.postCurrentSong(song)
        }

        private fun showNotification(
                currentSong: Song,
                bitmap: Bitmap,
                context: Context,
                intent: Intent,
                playPauseButton: Int,
                likeButton: Int
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


                val notificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                        NotificationManagerCompat.from(context.applicationContext)
                notificationManager.notify(1, notification)

        }

        fun getBitmapAndShowNotification(
                currentSong: Song, context: Context, intent: Intent, playPauseButton: Int,
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
                                                intent,
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
                                                intent,
                                                playPauseButton = playPauseButton,
                                                likeButton = likeButton
                                        )
                                }

                                override fun onLoadCleared(placeholder: Drawable?) {
                                }

                        })
        }

        private suspend fun insertAllSongs(context: Context) {
                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                val projection = arrayOf(
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ALBUM,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.ALBUM_ID
                )
                val cursor = context.contentResolver.query(uri, projection, null, null, null)
                cursor?.let {
                        while (it.moveToNext()) {
                                val title = cursor.getString(0)
                                val album = cursor.getString(1)
                                val artist = cursor.getString(2)
                                val duration = cursor.getString(3)
                                val id = cursor.getLong(4)
                                val albumId = cursor.getLong(5)

                                val sArtworkUri =
                                        Uri.parse("content://media/external/audio/albumart")
                                val albumArtUri =
                                        ContentUris.withAppendedId(sArtworkUri, albumId).toString()
                                val songUri = ContentUris.withAppendedId(
                                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                        id
                                ).toString()
                                val doesExist = repository.doesRowExist(songUri)

                                if (title != "" && !doesExist) {
                                        repository.insertSong(
                                                Song(
                                                        title = title,
                                                        album = album,
                                                        artist = artist,
                                                        duration = duration,
                                                        path = songUri,
                                                        imagePath = albumArtUri
                                                )
                                        )
                                        Log.d(TAG, "insertAllSongs: successFul")
                                }
                        }
                }
        }
}