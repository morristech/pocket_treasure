package com.stavro_xhardha.pockettreasure.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class SharedViewModel : ViewModel() {

    val updatedCountry = MutableLiveData<String>()

    fun updateCountry(country: String) {
        updatedCountry.value = country
    }

}