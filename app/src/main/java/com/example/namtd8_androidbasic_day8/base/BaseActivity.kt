package com.example.namtd8_androidbasic_day8.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.namtd8_androidbasic_day8.data.network.RemoteDataSource

abstract class BaseActivity<VM: ViewModel, B: ViewBinding, R:BaseRepository> : AppCompatActivity() {
    protected lateinit var binding: B
    lateinit var viewModel: VM
    protected val remoteDataSource = RemoteDataSource()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = getActivityBinding(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory(getActivityRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        handleTask()
    }

    abstract fun handleTask()

    abstract fun getViewModel(): Class<VM>
    abstract fun getActivityBinding(inflater: LayoutInflater) : B
    abstract fun getActivityRepository() : R
}