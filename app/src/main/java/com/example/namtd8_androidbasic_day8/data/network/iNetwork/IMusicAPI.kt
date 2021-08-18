package com.example.namtd8_androidbasic_day8.data.network.iNetwork

import com.example.namtd8_androidbasic_day8.data.models.AllMusicResponse
import retrofit2.http.GET

interface IMusicAPI {
    @GET("music.json")
    suspend fun getAllMusic() : AllMusicResponse
}