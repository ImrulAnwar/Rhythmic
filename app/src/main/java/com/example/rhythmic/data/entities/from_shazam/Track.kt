package com.example.rhythmic.data.entities.from_shazam

data class Track(
    val albumadamid: String,
    val artists: List<Artist>,
    val genres: Genres,
    val hub: Hub,
    val images: ImagesX,
    val isrc: String,
    val key: String,
    val layout: String,
    val myshazam: Myshazam,
    val sections: List<Section>,
    val share: ShareX,
    val subtitle: String,
    val title: String,
    val type: String,
    val url: String,
    val urlparams: Urlparams
)
