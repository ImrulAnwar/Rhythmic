package com.example.rhythmic.data.entities.from_shazam

data class Matches(
    val channel: String,
    val frequencyskew: Double,
    val id: String,
    val offset: Double,
    val timeskew: Double
)