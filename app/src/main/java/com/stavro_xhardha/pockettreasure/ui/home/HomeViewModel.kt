package com.stavro_xhardha.pockettreasure.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stavro_xhardha.pockettreasure.brain.ACCENT_BACKGROUND
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.WHITE_BACKGROUND
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalTime
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val homeRepository: HomeRepository
) : ViewModel() {

    val monthSection: MutableLiveData<String> = MutableLiveData()
    val locationSecton: MutableLiveData<String> = MutableLiveData()
    val fajrTime: MutableLiveData<String> = MutableLiveData()
    val dhuhrtime: MutableLiveData<String> = MutableLiveData()
    val asrTime: MutableLiveData<String> = MutableLiveData()
    val maghribTime: MutableLiveData<String> = MutableLiveData()
    val ishaTime: MutableLiveData<String> = MutableLiveData()
    val progressBarVisibility: MutableLiveData<Int> = MutableLiveData()
    val showErroToast: MutableLiveData<Boolean> = MutableLiveData()
    val contentVisibility: MutableLiveData<Int> = MutableLiveData()
    val fajrColor: MutableLiveData<Int> = MutableLiveData()
    val dhuhrColor: MutableLiveData<Int> = MutableLiveData()
    val asrColor: MutableLiveData<Int> = MutableLiveData()
    val maghribColor: MutableLiveData<Int> = MutableLiveData()
    val ishaColor: MutableLiveData<Int> = MutableLiveData()

    fun loadPrayerTimes() {
        viewModelScope.launch(Dispatchers.IO) {
            if (dateHasPassed()) {
                makePrayerApiCall()
            } else {
                withContext(Dispatchers.Main) {
                    setValuesToLiveData()
                }
            }
        }
    }

    private suspend fun makePrayerApiCall() {
        withContext(Dispatchers.Main) {
            switchProgressBarOn()
        }
        try {
            val todaysPrayerTime = homeRepository.makePrayerCallAsync().await()
            if (todaysPrayerTime.isSuccessful) {
                saveDataToShardPreferences(todaysPrayerTime.body())
                withContext(Dispatchers.Main) {
                    setValuesToLiveData()
                }
            } else {
                withContext(Dispatchers.Main){
                    showError()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main){
                showError()
            }
            e.printStackTrace()
        }
    }

    private fun showError() {
        showErroToast.value = true
        progressBarVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
    }

    private fun switchProgressBarOn() {
        progressBarVisibility.value = View.VISIBLE
        contentVisibility.value = View.GONE
        showErroToast.value = false
    }

    private fun switchProgressBarOff() {
        progressBarVisibility.value = View.GONE
        contentVisibility.value = View.VISIBLE
        showErroToast.value = false
    }

    private fun setValuesToLiveData() {
        putValues()
        findCurrentTime()
        switchProgressBarOff()
    }

    private fun putValues() {
        monthSection.value = homeRepository.readMonthSection()
        locationSecton.value = homeRepository.readLocationSection()
        fajrTime.value = homeRepository.readFejrtime()
        dhuhrtime.value = homeRepository.readDhuhrTime()
        asrTime.value = homeRepository.readAsrTime()
        maghribTime.value = homeRepository.readMaghribTime()
        ishaTime.value = homeRepository.readIshaTime()
    }

    private fun findCurrentTime() {
        val currentTime = LocalTime()
        try {
            val fajrTime = LocalTime(
                (homeRepository.readFejrtime()?.substring(0, 2)!!.toInt()),
                (homeRepository.readFejrtime()?.substring(3, 5))!!.toInt()
            )
            val dhuhrTime = LocalTime(
                (homeRepository.readDhuhrTime()?.substring(0, 2)!!.toInt()),
                (homeRepository.readDhuhrTime()?.substring(3, 5))!!.toInt()
            )
            val asrTime = LocalTime(
                (homeRepository.readAsrTime()?.substring(0, 2)!!.toInt()),
                (homeRepository.readAsrTime()?.substring(3, 5))!!.toInt()
            )
            val maghribTime = LocalTime(
                (homeRepository.readMaghribTime()?.substring(0, 2)!!.toInt()),
                (homeRepository.readMaghribTime()?.substring(3, 5))!!.toInt()
            )
            val ishaTime = LocalTime(
                (homeRepository.readIshaTime()?.substring(0, 2)!!.toInt()),
                (homeRepository.readIshaTime()?.substring(3, 5))!!.toInt()
            )

            if (isDebugMode) {
                Log.d(APPLICATION_TAG, " $currentTime")
                Log.d(APPLICATION_TAG, " $fajrTime")
                Log.d(APPLICATION_TAG, " $dhuhrTime")
                Log.d(APPLICATION_TAG, " $asrTime")
                Log.d(APPLICATION_TAG, " $maghribTime")
                Log.d(APPLICATION_TAG, " $ishaTime")
            }

            compareTiming(currentTime, fajrTime, dhuhrTime, asrTime, maghribTime, ishaTime)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    private fun compareTiming(
        currentTime: LocalTime,
        fajrTime: LocalTime,
        dhuhrTime: LocalTime,
        asrTime: LocalTime,
        maghribTime: LocalTime,
        ishaTime: LocalTime
    ) {
        if (currentTime.isAfter(fajrTime))
            if (currentTime.isAfter(dhuhrTime))
                if (currentTime.isAfter(asrTime))
                    if (currentTime.isAfter(maghribTime))
                        if (currentTime.isAfter(ishaTime))
                            switchFajrOn()
                        else
                            switchIshaOn()
                    else
                        switchMaghribOn()
                else
                    switchAsrOn()
            else
                switchDhuhrOn()
        else
            switchFajrOn()

    }

    private fun switchFajrOn() {
        fajrColor.value = ACCENT_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchDhuhrOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = ACCENT_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchAsrOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = ACCENT_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchMaghribOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = ACCENT_BACKGROUND
        ishaColor.value = WHITE_BACKGROUND
    }

    private fun switchIshaOn() {
        fajrColor.value = WHITE_BACKGROUND
        dhuhrColor.value = WHITE_BACKGROUND
        asrColor.value = WHITE_BACKGROUND
        maghribColor.value = WHITE_BACKGROUND
        ishaColor.value = ACCENT_BACKGROUND
    }

    private fun dateHasPassed(): Boolean {
        val date = DateTime()

        val currentDay = date.dayOfMonth
        val currentMonth = date.monthOfYear
        val currentYear = date.year

        val currentRegisteredDay = homeRepository.getCurrentRegisteredDay()
        val currentRegisteredMonth = homeRepository.getCurrentRegisteredMonth()
        val currentRegisteredYear = homeRepository.getCurrentRegisteredYear()

        return !(currentDay == currentRegisteredDay &&
                currentMonth == currentRegisteredMonth &&
                currentYear == currentRegisteredYear)
    }

    private fun saveDataToShardPreferences(prayerTimeResponse: PrayerTimeResponse?) {
        if (prayerTimeResponse != null) {
            try {
                saveThePrayerTimeResponseToMemory(prayerTimeResponse)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    private fun saveThePrayerTimeResponseToMemory(prayerTimeResponse: PrayerTimeResponse) {
        prayerTimeResponse.let {
            homeRepository.saveFajrTime(it.data.timings.fajr)
            homeRepository.saveDhuhrTime(it.data.timings.dhuhr)
            homeRepository.saveAsrTime(it.data.timings.asr)
            homeRepository.saveMagribTime(it.data.timings.magrib)
            homeRepository.saveIshaTime(it.data.timings.isha)
            homeRepository.saveDayOfMonth(it.data.date.gregorianDate.day.toInt())
            homeRepository.saveYear(it.data.date.gregorianDate.year.toInt())
            homeRepository.saveMonthOfYear(it.data.date.gregorianDate.gregorianMonth.number)
            homeRepository.saveMonthName(it.data.date.gregorianDate.gregorianMonth.monthNameInEnglish)
            homeRepository.saveDayOfMonthHijri(it.data.date.hijriPrayerDate.day)
            homeRepository.saveMonthOfYearHijri(it.data.date.hijriPrayerDate.hirjiMonth.monthNameInEnglish)
            homeRepository.saveYearHijri(it.data.date.hijriPrayerDate.year)
        }
    }
}