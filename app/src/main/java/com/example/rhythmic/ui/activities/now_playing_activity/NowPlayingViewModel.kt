package com.example.rhythmic.ui.activities.now_playing_activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rhythmic.data.entities.Song

class NowPlayingViewModel(application: Application) : AndroidViewModel(application) {
        var currentSong =  MutableLiveData<Song>()
        var seekPosition: Int = 0
}