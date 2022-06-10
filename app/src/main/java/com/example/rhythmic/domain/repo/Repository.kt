package com.example.rhythmic.domain.repo

import androidx.lifecycle.LiveData
import com.example.rhythmic.data.entities.Song

interface Repository {
        fun getAllSongs(): LiveData<List<Song>>
        fun getRecentSongs(): LiveData<List<Song>>
        fun getLikedSongs(): LiveData<List<Song>>
        fun getAllAlbums(): LiveData<List<Song>>
        fun getAllArtists(): LiveData<List<Song>>
        fun getCurrentSong(): LiveData<Song>
        fun setCurrentSong(song: Song)
        fun setIsPlaying(boolean: Boolean)
        fun isPlaying(): LiveData<Boolean>
        suspend fun insertSong(song: Song)
        suspend fun deleteSong(song: Song)
       suspend fun doesRowExist(path : String): Boolean
        fun setCurrentSongList(songList: List<Song>)
        fun getCurrentSongLIst(): List<Song>
}