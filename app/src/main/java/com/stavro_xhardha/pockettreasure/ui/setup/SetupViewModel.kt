package com.stavro_xhardha.pockettreasure.ui.setup

import android.view.View
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.stavro_xhardha.pockettreasure.brain.getDefaultMidnightImplementation
import com.stavro_xhardha.pockettreasure.brain.getMidnightImplementation
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SetupViewModel @Inject constructor(private val setupRepository: SetupRepository) : ViewModel() {

    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    val isCountryAndCapitalEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    val calendar: MutableLiveData<Calendar> = MutableLiveData()

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

    private suspend fun invokeMidnightCall(body: PrayerTimeResponse?) {
        withContext(Dispatchers.Main) {
            if (body != null) {
                calendar.value = getMidnightImplementation(body.data.timings.midnight)
            } else {
                calendar.value = getDefaultMidnightImplementation()
            }
        }
    }

    class OneTimeObserver<T>(private val handler: (T) -> Unit) : Observer<T>, LifecycleOwner {
        private val lifecycle = LifecycleRegistry(this)

        init {
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        }

        override fun getLifecycle(): Lifecycle = lifecycle

        override fun onChanged(t: T) {
            handler(t)
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        }
    }
}