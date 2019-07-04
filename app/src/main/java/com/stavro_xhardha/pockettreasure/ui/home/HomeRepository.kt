package com.stavro_xhardha.pockettreasure.ui.home

import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import retrofit2.Response
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val treasureApi: TreasureApi,
    private val mSharedPreferences: Rocket
) {
    //todo update to suspend :)

    suspend fun makePrayerCallAsync(): Response<PrayerTimeResponse> = treasureApi.getPrayerTimesTodayAsync(
        mSharedPreferences.readString(CAPITAL_SHARED_PREFERENCES_KEY),
        mSharedPreferences.readString(COUNTRY_SHARED_PREFERENCE_KEY)
    )

    fun saveFajrTime(fajr: String) {
        if (fajr.isNotEmpty())
            mSharedPreferences.writeString(FAJR_KEY, fajr)
    }

    fun saveDhuhrTime(dhuhr: String) {
        if (dhuhr.isNotEmpty())
            mSharedPreferences.writeString(DHUHR_KEY, dhuhr)
    }

    fun saveAsrTime(asr: String) {
        if (asr.isNotEmpty())
            mSharedPreferences.writeString(ASR_KEY, asr)
    }

    fun saveMagribTime(magrib: String) {
        if (magrib.isNotEmpty())
            mSharedPreferences.writeString(MAGHRIB_KEY, magrib)
    }

    fun saveIshaTime(isha: String) {
        if (isha.isNotEmpty())
            mSharedPreferences.writeString(ISHA_KEY, isha)
    }

    fun saveDayOfMonth(datyOfMonth: Int) {
        mSharedPreferences.writeInt(GREGORIAN_DAY_KEY, datyOfMonth)
    }

    fun saveYear(year: Int) {
        if (year > 0)
            mSharedPreferences.writeInt(GREGORIAN_YEAR_KEY, year)
    }

    fun saveMonthOfYear(month: Int) {
        if (month >= 0)
            mSharedPreferences.writeInt(GREGORIAN_MONTH_KEY, month)
    }

    fun saveMonthName(monthNameInEnglish: String) {
        if (monthNameInEnglish.isNotEmpty())
            mSharedPreferences.writeString(GREGORIAN_MONTH_NAME_KEY, monthNameInEnglish)
    }

    fun saveDayOfMonthHijri(day: String) {
        if (day.isNotEmpty())
            mSharedPreferences.writeString(HIRJI_DAY_OF_MONTH_KEY, day)
    }

    fun saveMonthOfYearHijri(monthNameHijri: String) {
        if (monthNameHijri.isNotEmpty())
            mSharedPreferences.writeString(HIJRI_MONTH_NAME_KEY, monthNameHijri)
    }

    fun saveYearHijri(year: String) {
        if (year.isNotEmpty())
            mSharedPreferences.writeString(HIJRI_YEAR_KEY, year)
    }

    fun saveMidnight(midnight: String) {
        if (midnight.isNotEmpty())
            mSharedPreferences.writeString(MIDNIGHT_KEY, midnight)
    }

    fun saveFinishFajrTime(sunrise: String) {
        mSharedPreferences.writeString(SUNRISE_KEY, sunrise)
    }

    fun readMonthSection(): String? {
        val hijriDay = mSharedPreferences.readString(HIRJI_DAY_OF_MONTH_KEY)
        val hijriMonthName = mSharedPreferences.readString(HIJRI_MONTH_NAME_KEY)
        val hijriYear = mSharedPreferences.readString(HIJRI_YEAR_KEY)
        val gregorianDay = mSharedPreferences.readInt(GREGORIAN_DAY_KEY)
        val gregorianMonth = mSharedPreferences.readString(GREGORIAN_MONTH_NAME_KEY)
        val gregorianYear = mSharedPreferences.readInt(GREGORIAN_YEAR_KEY)

        return " $hijriDay $hijriMonthName $hijriYear / $gregorianDay $gregorianMonth $gregorianYear"
    }

    fun readLocationSection(): String? {
        val capitalCity = mSharedPreferences.readString(CAPITAL_SHARED_PREFERENCES_KEY)
        val country = mSharedPreferences.readString(COUNTRY_SHARED_PREFERENCE_KEY)

        return " $capitalCity, $country"
    }

    fun readFejrtime(): String? = mSharedPreferences.readString(FAJR_KEY)

    fun readDhuhrTime(): String? = mSharedPreferences.readString(DHUHR_KEY)

    fun readAsrTime(): String? = mSharedPreferences.readString(ASR_KEY)

    fun readMaghribTime(): String? = mSharedPreferences.readString(MAGHRIB_KEY)

    fun readIshaTime(): String? = mSharedPreferences.readString(ISHA_KEY)

    fun readFinishFajrTime(): String? = mSharedPreferences.readString(SUNRISE_KEY)

    fun getCurrentRegisteredDay(): Int = mSharedPreferences.readInt(GREGORIAN_DAY_KEY)

    fun getCurrentRegisteredMonth(): Int = mSharedPreferences.readInt(GREGORIAN_MONTH_KEY)

    fun getCurrentRegisteredYear(): Int = mSharedPreferences.readInt(GREGORIAN_YEAR_KEY)

    fun countryHasBeenUpdated(): Boolean = mSharedPreferences.readBoolean(COUNTRY_UPDATED)

    fun updateCountryState() {
        mSharedPreferences.writeBoolean(COUNTRY_UPDATED, false)
    }

    fun isWorkerFired(): Boolean = mSharedPreferences.readBoolean(WORKER_FIRED_KEY)

    fun updateWorkerFired() {
        mSharedPreferences.writeBoolean(WORKER_FIRED_KEY, true)
    }
}