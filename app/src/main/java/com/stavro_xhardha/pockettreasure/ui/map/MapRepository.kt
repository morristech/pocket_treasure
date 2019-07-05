package com.stavro_xhardha.pockettreasure.ui.map

import com.stavro_xhardha.pockettreasure.brain.CAPITAL_SHARED_PREFERENCES_KEY
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket

class MapRepository(
    private val treasureApi: TreasureApi,
    private val rocket: Rocket
) {

    suspend fun readCurrentSavedCity(): String? = rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)

}