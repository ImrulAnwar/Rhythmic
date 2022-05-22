package com.example.rhythmic.ui.main_activity.fragments.home

import android.content.Context
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

        private val _text = MutableLiveData<String>().apply {
                value = "This is home Fragment"
        }
        val text: LiveData<String> = _text
        fun insertAllSongs(context: Context){

        }
}