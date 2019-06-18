package com.stavro_xhardha.pockettreasure.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class HomeViewModelFactory @Inject constructor(private val repository: HomeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}