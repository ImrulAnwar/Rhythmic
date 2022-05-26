package com.example.rhythmic.data.repo

import androidx.lifecycle.LiveData
import com.example.rhythmic.data.db.SongDao
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository

class SongRepository(private val dao: SongDao) : Repository {
        override fun getAllSongs(): LiveData<List<Song>> = dao.getAllSongs()

        override fun getRecentSongs(): LiveData<List<Song>> = dao.getRecentSongs()

        override fun getLikedSongs(): LiveData<List<Song>> = dao.getLikedSongs()

        override suspend fun insertSong(song: Song) = dao.insertSong(song)

        override suspend fun deleteSong(song: Song) = dao.deleteSong(song)

        override suspend fun doesRowExist(path: String): Boolean  = dao.doesRowExist(path)
}