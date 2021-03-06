package com.example.rhythmic.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "songs_table")
data class Song(
        @PrimaryKey(autoGenerate = true)
        val id: Int?=null,
        val title: String?=null,
        val album: String? = null,
        val artist: String?= null,
        val duration: String? = null,
        val path: String? = null,
        val imagePath: String? = null,
        var isRecent: Boolean = false,
        var isLiked: Boolean = false
):Serializable
