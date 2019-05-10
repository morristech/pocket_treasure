package com.stavro_xhardha.pockettreasure.ui.home

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.LocalTime
import javax.inject.Inject


class HomeViewModel @Inject constructor(var homeRepository: HomeRepository) : ViewModel() {

    private val completableJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + completableJob)

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
        coroutineScope.launch {
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
        val todaysPrayerTime = homeRepository.makePrayerCallAsync().await()
        if (todaysPrayerTime.isSuccessful) {
            saveDataToShardPreferences(todaysPrayerTime.body())
            withContext(Dispatchers.Main) {
                setValuesToLiveData()
            }
        } else {
            showError()
            if (isDebugMode)
                Log.d(APPLICATION_TAG, "WRONG IMPLEMENTED")
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
        monthSection.value = homeRepository.readMonthSection()
        locationSecton.value = homeRepository.readLocationSection()
        fajrTime.value = homeRepository.readFejrtime()
        dhuhrtime.value = homeRepository.readDhuhrTime()
        asrTime.value = homeRepository.readAsrTime()
        maghribTime.value = homeRepository.readMaghribTime()
        ishaTime.value = homeRepository.readIshaTime()
        findCurrentTime()
        switchProgressBarOff()
    }

    private fun findCurrentTime() {
        val currentTime = LocalTime()
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

    private fun switchIshaOn() {
        fajrColor.value = R.color.md_white_1000
        dhuhrColor.value = R.color.md_white_1000
        asrColor.value = R.color.md_white_1000
        maghribColor.value = R.color.md_white_1000
        ishaColor.value = R.color.colorAccent
    }

    private fun switchMaghribOn() {
        fajrColor.value = R.color.md_white_1000
        dhuhrColor.value = R.color.md_white_1000
        asrColor.value = R.color.md_white_1000
        maghribColor.value = R.color.colorAccent
        ishaColor.value = R.color.md_white_1000
    }

    private fun switchAsrOn() {
        fajrColor.value = R.color.md_white_1000
        dhuhrColor.value = R.color.md_white_1000
        asrColor.value = R.color.colorAccent
        maghribColor.value = R.color.md_white_1000
        ishaColor.value = R.color.md_white_1000
    }

    private fun switchDhuhrOn() {
        fajrColor.value = R.color.md_white_1000
        dhuhrColor.value = R.color.colorAccent
        asrColor.value = R.color.md_white_1000
        maghribColor.value = R.color.md_white_1000
        ishaColor.value = R.color.md_white_1000
    }

    private fun switchFajrOn() {
        fajrColor.value = R.color.colorAccent
        dhuhrColor.value = R.color.md_white_1000
        asrColor.value = R.color.md_white_1000
        maghribColor.value = R.color.md_white_1000
        ishaColor.value = R.color.md_white_1000
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
                homeRepository.saveFajrTime(prayerTimeResponse.data.timings.fajr)
                homeRepository.saveDhuhrTime(prayerTimeResponse.data.timings.dhuhr)
                homeRepository.saveAsrTime(prayerTimeResponse.data.timings.asr)
                homeRepository.saveMagribTime(prayerTimeResponse.data.timings.magrib)
                homeRepository.saveIshaTime(prayerTimeResponse.data.timings.isha)
                homeRepository.saveDayOfMonth(Integer.parseInt(prayerTimeResponse.data.date.gregorianDate.day))
                homeRepository.saveYear(Integer.parseInt(prayerTimeResponse.data.date.gregorianDate.year))
                homeRepository.saveMonthOfYear(prayerTimeResponse.data.date.gregorianDate.gregorianMonth.number)
                homeRepository.saveMonthName(prayerTimeResponse.data.date.gregorianDate.gregorianMonth.monthNameInEnglish)
                homeRepository.saveDayOfMonthHijri(prayerTimeResponse.data.date.hijriPrayerDate.day)
                homeRepository.saveMonthOfYearHijri(prayerTimeResponse.data.date.hijriPrayerDate.hirjiMonth.monthNameInEnglish)
                homeRepository.saveYearHijri(prayerTimeResponse.data.date.hijriPrayerDate.year)
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        completableJob.cancel()
    }
}