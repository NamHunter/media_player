package com.example.namtd8_androidbasic_day8.data.repository

import android.util.Log
import com.example.namtd8_androidbasic_day8.base.BaseRepository
import com.example.namtd8_androidbasic_day8.data.network.iNetwork.IMusicAPI
import com.example.namtd8_androidbasic_day8.utils.Const.BASE_URL

class MusicRepository(private val api: IMusicAPI
): BaseRepository() {

    suspend fun getAllMusicAPI() = safeApiCall {
        api.getAllMusic().music.onEach {
            item ->
            run {
                item.image = BASE_URL + item.image
                item.source = BASE_URL + item.source
            }
        }
    }
}

