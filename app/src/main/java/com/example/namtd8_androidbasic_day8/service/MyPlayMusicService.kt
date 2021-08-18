package com.example.namtd8_androidbasic_day8.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.bumptech.glide.Glide
import com.example.namtd8_androidbasic_day8.R
import com.example.namtd8_androidbasic_day8.lifecycle.MyMediaObserve
import com.example.namtd8_androidbasic_day8.media.MediaManager
import com.example.namtd8_androidbasic_day8.ui.main.MainActivity
import com.example.namtd8_androidbasic_day8.utils.Const.ACTION_CHANGE_STATE
import com.example.namtd8_androidbasic_day8.utils.Const.ACTION_NEXT
import com.example.namtd8_androidbasic_day8.utils.Const.ACTION_PREVIOUS
import com.example.namtd8_androidbasic_day8.utils.Const.ACTION_SEND_DATA
import com.example.namtd8_androidbasic_day8.utils.Const.CHANNEL_CODE
import com.example.namtd8_androidbasic_day8.utils.Const.EXTRA_ARTIST
import com.example.namtd8_androidbasic_day8.utils.Const.EXTRA_IMAGE
import com.example.namtd8_androidbasic_day8.utils.Const.EXTRA_TITTLE
import com.example.namtd8_androidbasic_day8.utils.Const.MEDIA_PAUSE
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.coroutineContext

class MyPlayMusicService: LifecycleService() {

    private val mMediaManager: MediaManager by lazy { MediaManager.getInstance(applicationContext) }

    private var mTitle: String? = null
    private var mArtist: String? = null
    private var mUrlImage: String? = null

    private val mReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                ACTION_NEXT ->{
                    mMediaManager.playNextSong()
                    createNotification(
                        viewIDPauseOrStop = R.drawable.ic_play_circle_outline_24)
                }
                ACTION_SEND_DATA ->{
                    Log.d("TAG", "onReceive: nhanlai")
                    mTitle = intent.getStringExtra(EXTRA_TITTLE)
                    mArtist = intent.getStringExtra(EXTRA_ARTIST)
                    mUrlImage = intent.getStringExtra(EXTRA_IMAGE)

                    createNotification(
                        viewIDPauseOrStop = R.drawable.ic_pause_circle_outline_24)
                }
                ACTION_PREVIOUS ->{
                    mMediaManager.playPreviousSong()

                    createNotification(
                        viewIDPauseOrStop = R.drawable.ic_play_circle_outline_24)
                }
                ACTION_CHANGE_STATE ->{
                    if(mMediaManager.mMediaState == MEDIA_PAUSE){
                        createNotification(
                            viewIDPauseOrStop = R.drawable.ic_pause_circle_outline_24)

                    }else{
                        createNotification(
                            viewIDPauseOrStop = R.drawable.ic_play_circle_outline_24)
                    }

                    mMediaManager.changState()
                }
            }
        }
    }

    var myBitmap: Bitmap? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        lifecycle.addObserver(MyMediaObserve(mMediaManager))
        init()
        super.onCreate()
    }

    private fun init(){
        initReceiver()
    }

    private fun initReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ACTION_NEXT)
        intentFilter.addAction(ACTION_PREVIOUS)
        intentFilter.addAction(ACTION_CHANGE_STATE)
        intentFilter.addAction(ACTION_SEND_DATA)

        registerReceiver(mReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("TAG", "onStartCommand: 1")

        createNotification()
        return START_STICKY
    }

    private fun createNotification(viewIDPauseOrStop : Int = R.drawable.ic_pause_circle_outline_24, title: String? = mTitle, artist: String? = mArtist){
        val mediaSession = MediaSessionCompat(this, "PlayService")
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this@MyPlayMusicService, 0,intent, 0)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_CODE)
            .setContentTitle(title?: "unknown")
            .setContentText(artist?: "unknown")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setGroupSummary(true)
            .addAction(R.drawable.ic_skip_previous_24, "previous", createPendingIntent(ACTION_PREVIOUS) )
            .addAction(viewIDPauseOrStop, "play", createPendingIntent(ACTION_CHANGE_STATE) )
            .addAction(R.drawable.ic_skip_next_24, "next", createPendingIntent(ACTION_NEXT) )
            .setColor(Color.BLACK)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0,1,2)
                .setMediaSession(mediaSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOnlyAlertOnce(true) //Set this flag if you would only like the sound, vibrate and ticker to be played if the notification is not already showing.

        mUrlImage?.let { applyImageUrl(notificationBuilder, it) }

        startForeground(1, notificationBuilder.build())
    }
    private fun createPendingIntent(actionName: String) : PendingIntent =
        Intent(actionName).let {
            PendingIntent.getBroadcast(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    private fun applyImageUrl(
        builder: NotificationCompat.Builder,
        imageUrl: String
    ) = runBlocking {
        val url = URL(imageUrl)

        withContext(IO) {
            try {
                val input = url.openStream()
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                null
            }
        }?.let { bitmap ->
            builder.setLargeIcon(bitmap)
        }
    }
}