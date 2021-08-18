package com.example.namtd8_androidbasic_day8.media

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import com.example.namtd8_androidbasic_day8.data.models.Music
import com.example.namtd8_androidbasic_day8.utils.Const.ACTION_SEND_DATA
import com.example.namtd8_androidbasic_day8.utils.Const.EXTRA_ARTIST
import com.example.namtd8_androidbasic_day8.utils.Const.EXTRA_IMAGE
import com.example.namtd8_androidbasic_day8.utils.Const.EXTRA_TITTLE
import com.example.namtd8_androidbasic_day8.utils.Const.MEDIA_PAUSE
import com.example.namtd8_androidbasic_day8.utils.Const.MEDIA_PLAY
import com.example.namtd8_androidbasic_day8.utils.SingletonHolder

class MediaManager private constructor(private val context: Context){
    private var mListSong = listOf<Music>()

    var mMediaState = MEDIA_PLAY
    var mCurrentIndex = 0

    private val mMediaManager = MediaPlayer()

    init {
        mMediaState = MEDIA_PLAY
    }

    fun release(){
        mMediaManager.release()
    }

    fun playOrPauseSong(isPlayAgain: Boolean){
        if(mMediaState == MEDIA_PLAY && isPlayAgain)
            try {
                mMediaManager.apply {
                    Intent(ACTION_SEND_DATA).apply {
                        putExtra(EXTRA_TITTLE, mListSong[mCurrentIndex].title)
                        putExtra(EXTRA_ARTIST, mListSong[mCurrentIndex].artist)
                        putExtra(EXTRA_IMAGE, mListSong[mCurrentIndex].image)
                        context.sendBroadcast(this)
                    }

                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    reset()
                    setDataSource(mListSong[mCurrentIndex].source)
                    prepare()
                    start()
                }
            }catch (e: Exception){
                e.printStackTrace()
                mMediaState = MEDIA_PAUSE
            }
        else if(mMediaState == MEDIA_PAUSE){
            mMediaManager.pause()
        }else{
            mMediaManager.apply {
                start()
            }
        }
    }

    fun playNextSong(){
        if(mCurrentIndex == mListSong.size - 1){
            mCurrentIndex = 0
        }else{
            mCurrentIndex++
        }
        playOrPauseSong(isPlayAgain = true)
    }

    fun playPreviousSong(){
        if(mCurrentIndex == 0){
            mCurrentIndex = mListSong.size - 1
        }else{
            mCurrentIndex --
        }

        playOrPauseSong(isPlayAgain = true)
    }

    fun changState(){
        mMediaState = if(mMediaState == MEDIA_PAUSE){
            MEDIA_PLAY
        }else{
            MEDIA_PAUSE
        }
        playOrPauseSong(isPlayAgain = false)
    }

    fun updateListSong(listSong: List<Music>){
        mListSong = listSong
        Log.d("TAG", "initObserve: da update")
    }

    companion object: SingletonHolder<MediaManager,Context>(::MediaManager)
}
