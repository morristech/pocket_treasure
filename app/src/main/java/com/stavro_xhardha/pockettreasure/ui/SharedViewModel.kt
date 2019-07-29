package com.stavro_xhardha.pockettreasure.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {
    private val _onGpsOpened = MutableLiveData<Boolean>()

    val onGpsOpened = _onGpsOpened

    fun onGpsOpened() {
        _onGpsOpened.value = true
    }
}