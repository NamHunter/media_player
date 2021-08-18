package com.example.namtd8_androidbasic_day8.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import com.example.namtd8_androidbasic_day8.utils.Const.CHANNEL_CODE
import com.example.namtd8_androidbasic_day8.utils.Const.CHANNEL_NAME


class Application : Application(){

    private fun createNotificationChannel(){
        val notificationManager =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(CHANNEL_CODE, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
            enableLights(true)
            lightColor = Color.RED
            enableVibration(true) // che do rung
        }

        notificationManager.createNotificationChannel(channel)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

}