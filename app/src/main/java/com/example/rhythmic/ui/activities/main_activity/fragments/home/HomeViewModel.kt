package com.example.rhythmic.ui.activities.main_activity.fragments.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
)  : AndroidViewModel(application) {

        fun getAllSongs(): LiveData<List<Song>> = repository.getAllSongs()

}