package com.stavro_xhardha.pockettreasure.ui.setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.model.Country
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetupViewModel @Inject constructor(
    private val setupRepository: SetupRepository,
    private val coroutinesDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val completableJob = Job()
    private val networkScope = CoroutineScope(coroutinesDispatcher.network + completableJob)
    val countriesList: MutableLiveData<ArrayList<Country>> = MutableLiveData()

    init {
        loadListOfCountries()
    }

    private fun loadListOfCountries() {
        networkScope.launch {
            val countriesListResponse = setupRepository.makeCountryApiCallAsync().await()
            withContext(coroutinesDispatcher.mainThread) {
                countriesList.value = countriesListResponse
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
