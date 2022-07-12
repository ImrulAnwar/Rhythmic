package com.example.rhythmic.data.api

import retrofit2.http.POST

interface ShazamApi {
        @POST("songs/v2/detect")
        suspend fun getSongId()
}