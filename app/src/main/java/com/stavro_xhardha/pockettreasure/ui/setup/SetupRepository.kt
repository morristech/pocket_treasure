package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import retrofit2.Response
import javax.inject.Inject

class SetupRepository @Inject constructor(
    private val treasureApi: TreasureApi,
    private val rocket: Rocket
) {

    suspend fun makeCountryApiCallAsync(): Response<ArrayList<Country>> =
        treasureApi.getCountriesListAsync(COUNTRIES_API_URL)

    fun saveCountryToSharedPreferences(country: Country) {
        rocket.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country.name)
        rocket.writeString(CAPITAL_SHARED_PREFERENCES_KEY, country.capitalCity)
    }

    fun isCountryOrCapitalEmpty(): Boolean {
        return rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)!!.isEmpty()
                || rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)!!.isEmpty()
    }

    fun switchNotificationFlags() {
        rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_ASR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, true)
    }

    suspend fun makePrayerCallAsync(): Response<PrayerTimeResponse> {
        val capitalCityName = rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)
        val countryName = rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)
        return treasureApi.getPrayerTimesTodayAsync(capitalCityName, countryName, 1)
    }

    fun notifyUserForFajr(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_FAJR)

    fun notifyUserForDhuhr(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)

    fun notifyUserForAsr(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ASR)

    fun notifyUserForMaghrib(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)

    fun notifyUserForIsha(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ISHA)
}