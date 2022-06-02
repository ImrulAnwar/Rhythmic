package com.example.rhythmic.ui.activities.main_activity.fragments.bottom_nav.home.top_nav.album

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository
import com.skydoves.viewmodel.lifecycle.viewModelLifecycleOwner
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "AlbumViewModel"

@HiltViewModel
class ArtistViewModel@Inject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {

        fun getAllArtists(): LiveData<List<Song>> = repository.getAllArtists()

}