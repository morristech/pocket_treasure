package com.stavro_xhardha.pockettreasure.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalTime
import javax.inject.Inject


class HomeViewModel @Inject constructor(
    var homeRepository: HomeRepository,
    var coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val completableJob = Job()
    private val networkScope = CoroutineScope(coroutineDispatcher.network + completableJob)

    val monthSection: MutableLiveData<String> = MutableLiveData()
    val locationSecton: MutableLiveData<String> = MutableLiveData()
    val fejrTime: MutableLiveData<String> = MutableLiveData()
    val dhuhrtime: MutableLiveData<String> = MutableLiveData()
    val asrTime: MutableLiveData<String> = MutableLiveData()
    val maghribTime: MutableLiveData<String> = MutableLiveData()
    val ishaTime: MutableLiveData<String> = MutableLiveData()
    val currentPrayerValue: MutableLiveData<Int> = MutableLiveData()

    init {
        loadPrayerTimes()
    }

    private fun loadPrayerTimes() {
        networkScope.launch {
            if (dateHasPassed()) {
                val todaysPrayerTime = homeRepository.makePrayerCallAsync().await()
                if (todaysPrayerTime.isSuccessful) {
                    withContext(coroutineDispatcher.disk) {
                        saveDataToShardPreferences(todaysPrayerTime.body())
                        withContext(coroutineDispatcher.mainThread) {
                            setValuesToLiveData()
                        }
                    }
                } else {
                    if (isDebugMode)
                        Log.d(APPLICATION_TAG, "WRONG IMPLEMENTED")
                }
            } else {
                withContext(coroutineDispatcher.mainThread) {
                    setValuesToLiveData()
                }
            }
        }
    }

    private fun setValuesToLiveData() {
        monthSection.value = homeRepository.readMonthSection()
        locationSecton.value = homeRepository.readLocationSection()
        fejrTime.value = homeRepository.readFejrtime()
        dhuhrtime.value = homeRepository.readDhuhrTime()
        asrTime.value = homeRepository.readAsrTime()
        maghribTime.value = homeRepository.readMaghribTime()
        ishaTime.value = homeRepository.readIshaTime()
        findCurrentTime()
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

        if (currentTime.isAfter(fajrTime))
            if (currentTime.isAfter(dhuhrTime))
                if (currentTime.isAfter(asrTime))
                    if (currentTime.isAfter(maghribTime))
                        if (currentTime.isAfter(ishaTime))
                            currentPrayerValue.value = FAJR
                        else
                            currentPrayerValue.value = ISHA
                    else
                        currentPrayerValue.value = MAGHRIB
                else
                    currentPrayerValue.value = ASR
            else
                currentPrayerValue.value = DHUHR
        else
            currentPrayerValue.value = FAJR

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
}