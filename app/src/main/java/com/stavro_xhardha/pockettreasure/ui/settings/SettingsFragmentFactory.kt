package com.stavro_xhardha.pockettreasure.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class SettingsFragmentFactory @Inject constructor(private val repository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = SettingsViewModel(repository) as T
}