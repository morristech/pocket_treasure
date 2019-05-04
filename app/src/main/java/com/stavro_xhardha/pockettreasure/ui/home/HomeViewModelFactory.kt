package com.stavro_xhardha.pockettreasure.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

class HomeViewModelFactory(private val provider: Provider<HomeViewModel>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider.get() as T
}