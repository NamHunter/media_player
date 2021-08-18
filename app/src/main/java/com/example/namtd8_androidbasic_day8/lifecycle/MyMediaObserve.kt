package com.example.namtd8_androidbasic_day8.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.namtd8_androidbasic_day8.media.MediaManager

class MyMediaObserve(private val mediaManager: MediaManager): LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun playMedia() {
        mediaManager.playOrPauseSong(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopMedia() {
        mediaManager.release()
    }
}