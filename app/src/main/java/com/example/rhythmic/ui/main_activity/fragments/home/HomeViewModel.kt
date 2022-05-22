package com.example.rhythmic.ui.main_activity.fragments.home

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.rhythmic.domain.repo.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val repository: SongRepository,
        application: Application
)  : AndroidViewModel(application) {
        private val REQUEST_CODE: Int = 1
        fun insertAllSongs(context: Context){
//                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

//        fun getRuntimePermission() {
//                if (ContextCompat.checkSelfPermission(
//                                getApplication<Application>().applicationContext,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE
//                        ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                        ActivityCompat.requestPermissions(
//                                getApplication<Application>(),
//                                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                                REQUEST_CODE
//                        )
//                } else {
//
//                }
//        }
}