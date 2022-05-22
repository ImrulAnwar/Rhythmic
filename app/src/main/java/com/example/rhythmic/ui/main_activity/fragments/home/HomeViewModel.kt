package com.example.rhythmic.ui.main_activity.fragments.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.example.rhythmic.domain.repo.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val repository: SongRepository,
        application: Application
)  : AndroidViewModel(application) {

        fun insertAllSongs(context: Context){
//                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
}