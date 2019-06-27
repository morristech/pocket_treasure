package com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog

import com.stavro_xhardha.pockettreasure.brain.CAPITAL_SHARED_PREFERENCES_KEY
import com.stavro_xhardha.pockettreasure.brain.COUNTRY_SHARED_PREFERENCE_KEY
import com.stavro_xhardha.pockettreasure.brain.COUNTRY_UPDATED
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.room_db.CountriesDao
import com.stavro_xhardha.rocket.Rocket
import javax.inject.Inject

class CountrySelectionRepository @Inject constructor(private val countriesDao: CountriesDao, val rocket: Rocket) {
    suspend fun selectAllCountries(): List<Country>? = countriesDao.selectAllCountries()

    fun updateCountry(country: Country) {
        rocket.writeString(COUNTRY_SHARED_PREFERENCE_KEY, country.name)
        rocket.writeString(CAPITAL_SHARED_PREFERENCES_KEY, country.capitalCity)
        rocket.writeBoolean(COUNTRY_UPDATED, true)
    }

    fun readCurrentCountry(): String = "${rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)} , ${rocket.readString(
        COUNTRY_SHARED_PREFERENCE_KEY
    )}"
}
