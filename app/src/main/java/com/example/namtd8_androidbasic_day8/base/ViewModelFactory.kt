package com.example.namtd8_androidbasic_day8.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.namtd8_androidbasic_day8.data.repository.MusicRepository
import com.example.namtd8_androidbasic_day8.viewModel.MainViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: BaseRepository): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when{
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository as MusicRepository) as T
            else -> throw IllegalArgumentException("ViewModel Not found")
        }
    }
}