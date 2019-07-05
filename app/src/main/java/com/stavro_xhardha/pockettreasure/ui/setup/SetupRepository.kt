package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.CountriesDao
import com.stavro_xhardha.rocket.Rocket
import retrofit2.Response
import javax.inject.Inject

class SetupRepository @Inject constructor(
    private val treasureApi: TreasureApi,
    private val rocket: Rocket,
    private val countriesDao: CountriesDao
) {

    suspend fun makeCountryApiCallAsync(): Response<ArrayList<Country>> =
        treasureApi.getCountriesListAsync(COUNTRIES_API_URL)

    suspend fun saveCountryToSharedPreferences(country: Country) {
        rocket.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country.name)
        rocket.writeString(CAPITAL_SHARED_PREFERENCES_KEY, country.capitalCity)
    }

    suspend fun isCountryOrCapitalEmpty(): Boolean {
        return rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)!!.isEmpty()
                || rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)!!.isEmpty()
    }

    suspend fun switchNotificationFlags() {
        rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_ASR, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, true)
        rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, true)
    }

    suspend fun saveCountriesToDatabase(body: ArrayList<Country>?) {
        body.let { body ->
            body!!.forEach { country ->
                countriesDao.insertCountry(country)
            }
        }
    }
}