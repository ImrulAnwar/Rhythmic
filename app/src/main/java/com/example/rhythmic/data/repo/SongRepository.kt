package com.example.rhythmic.data.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.rhythmic.data.db.SongDao
import com.example.rhythmic.data.entities.Song
import com.example.rhythmic.domain.repo.Repository

class SongRepository(private val dao: SongDao) : Repository {

    companion object {
        var currentSongList: List<Song> = emptyList()
        var currentSong: MutableLiveData<Song> = MutableLiveData()
        var currentSongPosition: Int = 0
        val isPlaying = MutableLiveData(true)
    }

    override fun setCurrentSongList(songList: List<Song>) {
        currentSongList = songList
    }

    override fun isPlaying() = isPlaying

    override fun setIsPlaying(boolean: Boolean) {
        isPlaying.value = boolean
    }

    override fun getCurrentSong(): LiveData<Song> = currentSong
    override fun setCurrentSong(song: Song) {
        currentSong.value = song
    }

    override fun postCurrentSong(song: Song) {
        currentSong.postValue(song)
    }

    override fun getCurrentSongPosition(): Int = currentSongPosition

    override fun setCurrentSongPosition(int: Int) {
        currentSongPosition = int
    }

    override fun getCurrentSongLIst(): List<Song> = currentSongList

    override fun getAllSongs(): LiveData<List<Song>> = dao.getAllSongs()

    override fun getRecentSongs(): LiveData<List<Song>> = dao.getRecentSongs()

    override fun getLikedSongs(): LiveData<List<Song>> = dao.getLikedSongs()

    override fun getAllAlbums(): LiveData<List<Song>> = dao.getAllAlbums()

    override fun getAllArtists(): LiveData<List<Song>> = dao.getAllArtists()

    override suspend fun insertSong(song: Song) = dao.insertSong(song)

    override suspend fun updateSong(song: Song) = dao.updateSong(song)

    override suspend fun deleteSong(song: Song) = dao.deleteSong(song)

    override suspend fun doesRowExist(path: String): Boolean = dao.doesRowExist(path)
}

