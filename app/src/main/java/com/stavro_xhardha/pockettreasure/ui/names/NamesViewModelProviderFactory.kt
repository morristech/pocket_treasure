package com.stavro_xhardha.pockettreasure.ui.names

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher

class NamesViewModelProviderFactory(
    private val repository: NamesRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = NamesViewModel(repository, coroutineDispatcher) as T
}