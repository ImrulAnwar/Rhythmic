package com.example.rhythmic.data.entities.from_shazam

data class Section(
        val actions: List<ActionXXXX>,
        val avatar: String,
        val beacondata: BeacondataX,
        val footer: String,
        val id: String,
        val metadata: List<Metadata>,
        val metapages: List<Metapage>,
        val name: String,
        val tabname: String,
        val text: List<String>,
        val type: String,
        val verified: Boolean,
        val youtubeurl: Youtubeurl
)