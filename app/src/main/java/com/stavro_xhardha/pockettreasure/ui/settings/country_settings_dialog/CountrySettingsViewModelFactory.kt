package com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class CountrySettingsViewModelFactory @Inject constructor(
    private val countrySelectionRepository: CountrySelectionRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        CountrySettingsViewModel(countrySelectionRepository) as T
}