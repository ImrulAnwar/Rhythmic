package com.example.rhythmic.ui.main_activity.fragments.home

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.SongRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val repository: SongRepository
)  : ViewModel() {

        fun insertAllSongs(context: Context){
//                val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }
}