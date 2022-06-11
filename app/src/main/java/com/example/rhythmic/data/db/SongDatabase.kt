package com.example.rhythmic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rhythmic.data.entities.Song

@Database(entities = [Song::class], version = 2)
abstract class SongDatabase : RoomDatabase() {
        abstract val songDao: SongDao
        companion object {
                const val DATABASE_NAME = "songs_db"
        }
}