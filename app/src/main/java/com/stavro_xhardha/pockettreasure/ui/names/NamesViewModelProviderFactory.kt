package com.stavro_xhardha.pockettreasure.ui.names

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NamesViewModelProviderFactory(
    private val repository: NamesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NamesViewModel(repository) as T
}