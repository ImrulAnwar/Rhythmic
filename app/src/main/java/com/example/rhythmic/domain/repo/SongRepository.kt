package com.example.rhythmic.domain.repo

import androidx.lifecycle.LiveData
import com.example.rhythmic.data.entities.Song

interface SongRepository {
        fun getAllSongs(): LiveData<List<Song>>
        fun getRecentSongs(): LiveData<List<Song>>
        fun getLikedSongs(): LiveData<List<Song>>
        suspend fun insertSong(song: Song)
        suspend fun deleteSong(song: Song)
}