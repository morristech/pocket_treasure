package com.stavro_xhardha.pockettreasure.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

class SetupViewModelFactory(private val provider: Provider<SetupViewModel>) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = provider.get() as T
}