package com.stavro_xhardha.pockettreasure.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {

    val updatedCountry = MutableLiveData<String>()
    private val _onGpsOpened = MutableLiveData<Boolean>()
    val onGpsOpened = _onGpsOpened

    fun updateCountry(country: String) {
        updatedCountry.value = country
    }

    fun onGpsOpened() {
        _onGpsOpened.value = true
    }

}