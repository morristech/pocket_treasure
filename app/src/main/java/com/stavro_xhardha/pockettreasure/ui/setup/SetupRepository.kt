package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.brain.COUNTRIES_API_URL
import com.stavro_xhardha.pockettreasure.brain.MSharedPreferences
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import javax.inject.Inject

class SetupRepository @Inject constructor(
    private val treasureApi: TreasureApi,
    private val mSharedPreferences: MSharedPreferences
) {
    suspend fun makeCountryApiCallAsync() = treasureApi.getCountriesList(COUNTRIES_API_URL)
}