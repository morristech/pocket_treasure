package com.stavro_xhardha.pockettreasure.ui.setup

import android.database.sqlite.SQLiteException
import android.location.Geocoder
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class SetupViewModel @Inject constructor(private val setupRepository: SetupRepository) : ViewModel() {

    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    private val _locationSettingsRequest = MutableLiveData<Boolean>()
    private val _locationRequestTurnOff = MutableLiveData<Boolean>()
    private val _serviceNotAvailableVisibility = MutableLiveData<Boolean>()
    private val _locationErrorVisibility = MutableLiveData<Boolean>()
    private val _prayerNotificationDialogVisibility = MutableLiveData<Boolean>()
    private val _locationProvided: MutableLiveData<Boolean> = MutableLiveData()

    val locationSettingsRequest: LiveData<Boolean> = _locationSettingsRequest
    val locationRequestTurnOff: LiveData<Boolean> = _locationRequestTurnOff
    val serviceNotAvailableVisibility: LiveData<Boolean> = _serviceNotAvailableVisibility
    val locationerrorVisibility: LiveData<Boolean> = _locationErrorVisibility
    val locationProvided: LiveData<Boolean> = _locationProvided
    val prayerNotificationDialogViaibility: LiveData<Boolean> = _prayerNotificationDialogVisibility
    private var locationTurnOfRequested: Boolean = false

    private lateinit var listOfCountries: ArrayList<Country>

    init {
        checkLocationsettings()
    }

    fun loadListOfCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            if (setupRepository.isCountryOrCapitalEmpty()) {
                makeCountriesApiCall()
                _locationProvided.postValue(true)
            } else {
                _locationProvided.postValue(false)
            }
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
        viewModelScope.launch {
            setupRepository.saveCountryToSharedPreferences(country)
        }
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

    private fun checkLocationsettings() {
        viewModelScope.launch {
            if (setupRepository.isLocationProvided()) {
                withContext(Dispatchers.IO) {
                    _locationProvided.postValue(true)
                }
            } else {
                _locationSettingsRequest.postValue(true)
            }
        }
    }

    fun convertToAdress(geocoder: Geocoder, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val adresses = geocoder.getFromLocation(latitude, longitude, 1)
                if (adresses.size != 0) {
                    val cityName = adresses[0].adminArea
                    val country = adresses[0].countryName
                    setupRepository.updateCountryAndLocation(country, cityName, latitude, longitude)
                    if (!locationTurnOfRequested) {
                        _prayerNotificationDialogVisibility.postValue(true)
                        locationTurnOfRequested = true
                    }
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
                _serviceNotAvailableVisibility.postValue(true)
                setupRepository.writeDefaultValues()
            } catch (illegalArgumentException: IllegalArgumentException) {
                illegalArgumentException.printStackTrace()
                _locationErrorVisibility.postValue(true)
                setupRepository.writeDefaultValues()
            } finally {
                _locationRequestTurnOff.postValue(true)
            }
        }
    }
}