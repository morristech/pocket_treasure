package com.stavro_xhardha.pockettreasure.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.brain.*
//import com.sxhardha.smoothie.Smoothie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsViewModel @Inject constructor(private val settingsRepository: SettingsRepository) : ViewModel() {
    private val _fajrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _dhuhrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _asrCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _maghribCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _ishaCheck: MutableLiveData<Boolean> = MutableLiveData()
    private val _countryAndCapital: MutableLiveData<String> = MutableLiveData()

    val fajrCheck: LiveData<Boolean> = _fajrCheck
    val dhuhrCheck: LiveData<Boolean> = _dhuhrCheck
    val asrCheck: LiveData<Boolean> = _asrCheck
    val maghribCheck: LiveData<Boolean> = _maghribCheck
    val ishaCheck: LiveData<Boolean> = _ishaCheck
    val countryAndCapital: LiveData<String> = _countryAndCapital

    private var fajrCheckHelper: Boolean = false
    private var dhuhrCheckHelper: Boolean = false
    private var asrCheckCheckHelper: Boolean = false
    private var mahgribCheckCheckHelper: Boolean = false
    private var ishaCheckCheckHelper: Boolean = false

    init {
        listenToRepository()
    }

    private fun listenToRepository() {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.let {
                fajrCheckHelper = it.getFajrChecked()
                dhuhrCheckHelper = it.getDhuhrChecked()
                asrCheckCheckHelper = it.getAsrChecked()
                mahgribCheckCheckHelper = it.getMaghribChecked()
                ishaCheckCheckHelper = it.getIshaChecked()
            }
            withContext(Dispatchers.Main) {
                this@SettingsViewModel._fajrCheck.value = fajrCheckHelper
                this@SettingsViewModel._dhuhrCheck.value = dhuhrCheckHelper
                this@SettingsViewModel._asrCheck.value = asrCheckCheckHelper
                this@SettingsViewModel._maghribCheck.value = mahgribCheckCheckHelper
                this@SettingsViewModel._ishaCheck.value = ishaCheckCheckHelper
                this@SettingsViewModel._countryAndCapital.value = settingsRepository.readCountryAndCapital()
                decrementIdlingResource()
            }
        }
    }

    fun onSwFajrClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putFajrNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _fajrCheck.value = settingsRepository.getFajrChecked()
            }
        }
    }

    fun onSwDhuhrClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putDhuhrNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _dhuhrCheck.value = settingsRepository.getDhuhrChecked()
            }
        }
    }

    fun onSwAsrClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putAsrNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _asrCheck.value = settingsRepository.getAsrChecked()
            }
        }
    }

    fun onSwMaghribClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putMaghribNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _maghribCheck.value = settingsRepository.getMaghribChecked()
            }
        }
    }

    fun onSwIshaClick(checked: Boolean) {
        incrementIdlingResource()
        viewModelScope.launch {
            settingsRepository.putIshaNotification(checked)
            withContext(Dispatchers.Main) {
                decrementIdlingResource()
                _ishaCheck.value = settingsRepository.getIshaChecked()
            }
        }
    }

    fun resetDataForWorker() {
        viewModelScope.launch {
            settingsRepository.deleteAllDataInside()
            startWorkManager()
        }
    }
}