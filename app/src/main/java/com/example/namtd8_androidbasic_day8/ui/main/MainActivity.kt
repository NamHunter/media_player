package com.example.namtd8_androidbasic_day8.ui.main

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.namtd8_androidbasic_day8.base.BaseActivity
import com.example.namtd8_androidbasic_day8.callback.ItemClickListener
import com.example.namtd8_androidbasic_day8.data.network.iNetwork.IMusicAPI
import com.example.namtd8_androidbasic_day8.data.repository.MusicRepository
import com.example.namtd8_androidbasic_day8.databinding.ActivityMainBinding
import com.example.namtd8_androidbasic_day8.media.MediaManager
import com.example.namtd8_androidbasic_day8.service.MyPlayMusicService
import com.example.namtd8_androidbasic_day8.ui.main.adapters.RecyclerDisplayMusicAdapter
import com.example.namtd8_androidbasic_day8.utils.Resource
import com.example.namtd8_androidbasic_day8.viewModel.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding, MusicRepository>(), ItemClickListener {

    private val mMediaManager: MediaManager by lazy { MediaManager.getInstance(applicationContext) }

    private val mAdapter by lazy {RecyclerDisplayMusicAdapter(this)}

    override fun handleTask() {
        init()

        getDataFromServer()
    }

    private fun play(){
        Intent(this, MyPlayMusicService::class.java).apply {
            startForegroundService(this)
        }
    }

    private fun init(){
        initView()
        initObserve()
    }

    private fun initView() {
        binding.recycleView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun initObserve() {
        viewModel.listMusics.observe(this, { itemResponse ->
            mAdapter.updateData(itemResponse)
            mMediaManager.updateListSong(itemResponse)
        })

        viewModel.currentIndex.observe(this, { musicIndex ->
            mMediaManager.mCurrentIndex = musicIndex

            mMediaManager.playOrPauseSong(true)
        })

        viewModel.eventError.observe(this, {
            Toast.makeText(this, it.getContentIfNotHandled(), Toast.LENGTH_SHORT).show()
        })
    }

    private fun getDataFromServer() {
        viewModel.fetchAllMusics()
    }

    override fun onCellClickListener(musicIndex: Int) {
        play()
        viewModel.setCurrentMusicIndex(musicIndex)
    }

    override fun getViewModel(): Class<MainViewModel> = MainViewModel::class.java

    override fun getActivityBinding(inflater: LayoutInflater) = ActivityMainBinding.inflate(layoutInflater)

    override fun getActivityRepository() = MusicRepository(remoteDataSource.buildAPI(IMusicAPI::class.java))
}