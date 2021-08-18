package com.example.namtd8_androidbasic_day8.data.models

import com.example.namtd8_androidbasic_day8.utils.Const.BASE_URL

data class Music(
    val album: String,
    val artist: String,
    val duration: Int,
    val site: String,
    var source: String,
    val title: String,
    val totalTrackCount: Int,
    val trackNumber: Int,
    var image: String,
    val genre: String
)