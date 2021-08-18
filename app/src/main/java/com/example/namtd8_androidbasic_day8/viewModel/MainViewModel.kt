package com.example.namtd8_androidbasic_day8.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.namtd8_androidbasic_day8.base.BaseViewModel
import com.example.namtd8_androidbasic_day8.data.models.Music
import com.example.namtd8_androidbasic_day8.data.repository.MusicRepository
import com.example.namtd8_androidbasic_day8.utils.Const.ERROR_NETWORK
import com.example.namtd8_androidbasic_day8.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MusicRepository) : BaseViewModel() {

    var listMusics = MutableLiveData<MutableList<Music>>()
        private set

    var currentIndex = MutableLiveData<Int>()
        private set

    fun fetchAllMusics() = viewModelScope.launch {
        repository.getAllMusicAPI().also {
            when (it) {
                is Resource.Success -> {
                    listMusics.value = it.value!!
                }
                is Resource.Failure -> {
                    if (it.isNetworkError) {
                        showError(ERROR_NETWORK)
                    } else {
                        when (it.errorCode) {
                            404 -> showError("Not Found Page !!")
                            else -> showError("Cant get data from server")
                        }
                    }
                }
                else -> throw IllegalArgumentException("We don't know this state")
            }
        }
    }

    fun setCurrentMusicIndex(index: Int) {
        currentIndex.value = index
    }
}