package com.stavro_xhardha.pockettreasure.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MapViewModelProviderFactory(private val mapRepository: MapRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MapViewModel(mapRepository) as T
}