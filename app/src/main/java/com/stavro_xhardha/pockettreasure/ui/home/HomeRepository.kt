package com.stavro_xhardha.pockettreasure.ui.home

import com.stavro_xhardha.pockettreasure.brain.MSharedPreferences
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import javax.inject.Inject

class HomeRepository @Inject constructor(
    treasureApi: TreasureApi,
    mSharedPreferences: MSharedPreferences
) {
}