package com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CountrySettingsViewModel @Inject constructor(private val countrySelectionRepository: CountrySelectionRepository) : ViewModel() {
    private val _countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    private val _isFragmentReadyToClose: MutableLiveData<Boolean> = MutableLiveData()
    private val _countryAndCapital = MutableLiveData<String>()
    val countriesList: LiveData<ArrayList<Country>> = _countriesList
    val countryAndCapital = _countryAndCapital
    val fragmentReadyToClose: LiveData<Boolean> = _isFragmentReadyToClose

    init {
        getCountriesData()
    }

    private fun getCountriesData() {
        viewModelScope.launch(Dispatchers.IO) {
            val countries = countrySelectionRepository.selectAllCountries()
            withContext(Dispatchers.Main) {
                _countriesList.value = ArrayList(countries)
            }
        }
    }

    fun newCountrySelected(country: Country) {
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionRepository.updateCountry(country)
            val currentCountry = countrySelectionRepository.readCurrentCountry()
            withContext(Dispatchers.Main) {
                _countryAndCapital.value = currentCountry
                _isFragmentReadyToClose.value = true
            }
        }
    }
}