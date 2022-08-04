package com.example.rhythmic.data.api

import com.example.rhythmic.data.entities.from_shazam.DetectionResponse
import retrofit2.Response
import retrofit2.http.POST

interface ShazamApi {
        @POST("songs/v2/detect")
        suspend fun getSongByRawSound(

        ): Response<DetectionResponse>
}