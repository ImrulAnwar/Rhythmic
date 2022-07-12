package com.example.rhythmic.data.entities.from_shazam

data class DetectionResponse(
        val matches: List<Matches>,
        val tagid: String,
        val timestamp: Long,
        val timezone: String,
        val track: Track
)