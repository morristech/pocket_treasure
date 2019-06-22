package com.stavro_xhardha.pockettreasure.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;

class MapViewModel(private val mapRepository: MapRepository) : ViewModel() {

    val city: MutableLiveData<String> = MutableLiveData()

    fun initMapData() {
        city.value = mapRepository.readCurrentSavedCity()
    }
}
