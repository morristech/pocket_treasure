package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class SetupRepository @Inject constructor(
    private val treasureApi: TreasureApi,
    private val mSharedPreferences: Rocket
) {

    fun makeCountryApiCallAsync(): Deferred<Response<ArrayList<Country>>> =
        treasureApi.getCountriesListAsync(COUNTRIES_API_URL)

    fun saveCountryToSharedPreferences(country: Country) {
        mSharedPreferences.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country.name)
        mSharedPreferences.writeString(CAPITAL_SHARED_PREFERENCES_KEY, country.capitalCity)
    }

    fun saveWakingUpUser() {
        mSharedPreferences.writeBoolean(WAKE_UP_USER_FOR_FAJR, true)
    }

    fun isCountryEmpty(): Boolean {
        return mSharedPreferences.readString(COUNTRY_SHARED_PREFERENCE_KEY)!!.isEmpty()
                || mSharedPreferences.readString(CAPITAL_SHARED_PREFERENCES_KEY)!!.isEmpty()
    }
}