package com.stavro_xhardha.pockettreasure.ui.setup

import android.database.sqlite.SQLiteException
import android.view.View
import androidx.lifecycle.*
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SetupViewModel @Inject constructor(private val setupRepository: SetupRepository) : ViewModel() {

    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    val isCountryAndCapitalEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    private lateinit var listOfCountries: ArrayList<Country>

    init {
        loadListOfCountries()
    }

    fun loadListOfCountries() {
        if (setupRepository.isCountryOrCapitalEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                makeCountriesApiCall()
            }
            isCountryAndCapitalEmpty.value = true
        } else {
            isCountryAndCapitalEmpty.value = false
        }
    }

    suspend fun makeCountriesApiCall() {
        withContext(Dispatchers.Main) {
            switchProgressBarOn()
        }
        try {
            val countriesListResponse = setupRepository.makeCountryApiCallAsync()
            if (countriesListResponse.isSuccessful) {
                listOfCountries = countriesListResponse.body()!!
                withContext(Dispatchers.Main) {
                    showContent()
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

    fun switchProgressBarOn() {
        pbVisibility.value = View.VISIBLE
        contentVisibility.value = View.GONE
        errorVisibility.value = View.GONE
    }

    fun showContent() {
        pbVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
        errorVisibility.value = View.GONE
    }

    fun showErrorLayout() {
        pbVisibility.value = View.GONE
        contentVisibility.value = View.GONE
        errorVisibility.value = View.VISIBLE
    }

    fun onCountrySelected(country: Country) {
        setupRepository.saveCountryToSharedPreferences(country)
    }

    fun updateNotificationFlags() {
        viewModelScope.launch(Dispatchers.IO) {
            setupRepository.switchNotificationFlags()
        }
    }

    fun saveCountriesToDatabase() {
        viewModelScope.launch {
            try {
                setupRepository.saveCountriesToDatabase(listOfCountries)
            } catch (e: SQLiteException) {
                e.printStackTrace()
            }
        }
    }
}