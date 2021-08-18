package com.example.namtd8_androidbasic_day8.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    var eventLoading = MutableLiveData<Event<Boolean>>()
        private set
    var eventError = MutableLiveData<Event<String>>()
        private set

    fun showLoading(value: Boolean) {
        eventLoading.value = Event(value)
    }

    fun showError(ErrorString: String) {
        eventError.value = Event(ErrorString)
    }
}