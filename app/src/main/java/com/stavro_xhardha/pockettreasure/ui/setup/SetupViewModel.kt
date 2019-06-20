package com.stavro_xhardha.pockettreasure.ui.setup

import android.view.View
import androidx.lifecycle.*
import com.stavro_xhardha.pockettreasure.brain.getCurrentDayPrayerImplementation
import com.stavro_xhardha.pockettreasure.brain.getDefaultMidnightImplementation
import com.stavro_xhardha.pockettreasure.brain.getMidnightImplementation
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.Calendar.getInstance
import javax.inject.Inject

class SetupViewModel @Inject constructor(private val setupRepository: SetupRepository) : ViewModel() {

    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    val isCountryAndCapitalEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    val tomorowsTime: MutableLiveData<Calendar> = MutableLiveData()
    val fajrTime: MutableLiveData<Calendar> = MutableLiveData()
    val dhuhrTime: MutableLiveData<Calendar> = MutableLiveData()
    val asrTime: MutableLiveData<Calendar> = MutableLiveData()
    val maghribTime: MutableLiveData<Calendar> = MutableLiveData()
    val ishaTime: MutableLiveData<Calendar> = MutableLiveData()

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

    fun scheduleSynchronisationTime() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prayerCallResponse = setupRepository.makePrayerCallAsync()
                if (prayerCallResponse.isSuccessful) {
                    invokeTodaysPrayerTimes(prayerCallResponse.body())
                    invokeMidnightCall(prayerCallResponse.body())
                } else {
                    invokeMidnightCall(null)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                invokeMidnightCall(null)
            }
        }
    }

    private suspend fun invokeTodaysPrayerTimes(body: PrayerTimeResponse?) {
        val currentTime = getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        withContext(Dispatchers.Main) {
            if (body != null) {
                if (currentTime.before(getCurrentDayPrayerImplementation(body.data.timings.fajr)))
                    fajrTime.value = getCurrentDayPrayerImplementation(body.data.timings.fajr)
                if (currentTime.before(getCurrentDayPrayerImplementation(body.data.timings.dhuhr)))
                    dhuhrTime.value = getCurrentDayPrayerImplementation(body.data.timings.dhuhr)
                if (currentTime.before(getCurrentDayPrayerImplementation(body.data.timings.asr)))
                    asrTime.value = getCurrentDayPrayerImplementation(body.data.timings.asr)
                if (currentTime.before(getCurrentDayPrayerImplementation(body.data.timings.magrib)))
                    maghribTime.value = getCurrentDayPrayerImplementation(body.data.timings.magrib)
                if (currentTime.before(getCurrentDayPrayerImplementation(body.data.timings.isha)))
                    ishaTime.value = getCurrentDayPrayerImplementation(body.data.timings.isha)
            }
        }
    }

    private suspend fun invokeMidnightCall(body: PrayerTimeResponse?) {
        withContext(Dispatchers.Main) {
            if (body != null) {
                tomorowsTime.value = getMidnightImplementation(body.data.timings.midnight)
            } else {
                tomorowsTime.value = getDefaultMidnightImplementation()
            }
        }
    }
}