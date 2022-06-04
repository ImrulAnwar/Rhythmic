package com.example.rhythmic.ui.activities.now_playing_activity

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.MediaPlayerFunctions
import com.example.rhythmic.domain.repo.Repository
import com.example.rhythmic.services.MusicService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

private const val TAG = "NowPlayingActivityViewModel"

@HiltViewModel
class NowPlayingViewModel @Inject constructor(
        private val repository: Repository,
        application: Application
) : AndroidViewModel(application) {
        var currentSong = MutableLiveData<Song>()

        var seekPosition: Int = 0


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

}