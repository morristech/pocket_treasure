package com.stavro_xhardha.pockettreasure.ui.setup

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetupViewModel @Inject constructor(private val setupRepository: SetupRepository) : ViewModel() {

    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    val isCountryAndCapitalEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadListOfCountries() {
        if (setupRepository.isCountryEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                makeCountriesApiCall()
            }
            isCountryAndCapitalEmpty.value = true
        } else {
            isCountryAndCapitalEmpty.value = false
        }
    }

    private suspend fun makeCountriesApiCall() {
        withContext(Dispatchers.Main) {
            switchProgressBarOn()
        }
        try {
            val countriesListResponse = setupRepository.makeCountryApiCallAsync()
            if (countriesListResponse.isSuccessful) {
                withContext(Dispatchers.Main) {
                    switchProgressOff()
                    countriesList.value = countriesListResponse.body()
                }
            } else {
                withContext(Dispatchers.Main) {
                    showErrorLayout()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                showErrorLayout()
            }
        }
    }

    private fun switchProgressBarOn() {
        pbVisibility.value = View.VISIBLE
        contentVisibility.value = View.GONE
        errorVisibility.value = View.GONE
    }

    private fun switchProgressOff() {
        pbVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
        errorVisibility.value = View.GONE
    }

    private fun showErrorLayout() {
        pbVisibility.value = View.GONE
        contentVisibility.value = View.GONE
        errorVisibility.value = View.VISIBLE
    }

    fun onCountrySelected(country: Country) {
        setupRepository.saveCountryToSharedPreferences(country)
    }
}