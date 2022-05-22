package com.example.rhythmic.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.rhythmic.data.entities.Song
import kotlinx.coroutines.flow.StateFlow

@Dao
interface SongDao {
        @Query("SELECT * FROM songs_table")
        fun getAllSongs(): LiveData<List<Song>>

        @Query("SELECT * FROM songs_table WHERE isRecent = :isRecent")
        fun getRecentSongs(isRecent: Boolean = true): LiveData<List<Song>>

        @Query("SELECT * FROM songs_table WHERE isLiked = :isLiked")
        fun getLikedSongs(isLiked: Boolean = true): LiveData<List<Song>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        suspend fun insertSong(song: Song)

        @Delete
        suspend fun deleteSong(song: Song)
}