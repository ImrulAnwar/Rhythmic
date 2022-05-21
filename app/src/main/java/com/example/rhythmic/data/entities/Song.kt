package com.example.rhythmic.data.entities

data class Song(
        val id: Int?=null,
        val title: String?=null,
        val album: String? = null,
        val artist: String?= null,
        val duration: String? = null,
        val path: String? = null,
        val imagePath: String? = null
)
