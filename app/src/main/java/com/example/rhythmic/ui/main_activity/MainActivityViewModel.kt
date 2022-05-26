package com.example.rhythmic.ui.main_activity

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

private val tag= "MainActivityViewModel"
@HiltViewModel
class MainActivityViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {
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
                                        Log.d(tag, "insertAllSongs: successFul")
                                }
                        }
                }
        }
}