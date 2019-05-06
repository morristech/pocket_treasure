package com.stavro_xhardha.pockettreasure.ui.setup

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.UnknownHostException
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupRepository,
    private val coroutinesDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val completableJob = Job()
    private val networkScope = CoroutineScope(coroutinesDispatcher.network + completableJob)
    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()
    val isCountryAndCapitalEmpty: MutableLiveData<Boolean> = MutableLiveData()
    val pbVisibility: MutableLiveData<Int> = MutableLiveData()
    val errorVisibility: MutableLiveData<Int> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()

    fun loadListOfCountries() {
        if (setupRepository.isCountryEmpty()) {
            networkScope.launch {
                makeCountriesApiCall()
            }
            isCountryAndCapitalEmpty.value = true
        } else {
            isCountryAndCapitalEmpty.value = false
        }
    }

    private suspend fun makeCountriesApiCall() {
        withContext(coroutinesDispatcher.mainThread) {
            switchProgressBarOn()
        }
        try {
            val countriesListResponse = setupRepository.makeCountryApiCallAsync().await()
            if (countriesListResponse.isSuccessful) {
                withContext(coroutinesDispatcher.mainThread) {
                    switchProgressOff()
                    countriesList.value = countriesListResponse.body()
                }
            } else {
                withContext(coroutinesDispatcher.mainThread) {
                    showErrorLayout()
                }
            }
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            withContext(coroutinesDispatcher.mainThread) {
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

    fun onYesDialogClicked() {
        setupRepository.saveWakingUpUser()
    }

    override fun onCleared() {
        super.onCleared()
        completableJob.cancel()
    }
}
