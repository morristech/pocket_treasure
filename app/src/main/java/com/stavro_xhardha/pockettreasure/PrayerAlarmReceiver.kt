package com.stavro_xhardha.pockettreasure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.crashlytics.android.Crashlytics
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.CAPITAL_SHARED_PREFERENCES_KEY
import com.stavro_xhardha.pockettreasure.brain.COUNTRY_SHARED_PREFERENCE_KEY
import com.stavro_xhardha.pockettreasure.dependency_injection.DaggerPocketTreasureComponent
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class PrayerAlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var treasureApi: TreasureApi

    override fun onReceive(context: Context?, intent: Intent?) {
        performDependencyInjection(context?.applicationContext)
        //it's midnight
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val prayerTimesResponse = treasureApi.getPrayerTimesTodayAsync(
                    CAPITAL_SHARED_PREFERENCES_KEY,
                    COUNTRY_SHARED_PREFERENCE_KEY, 1
                )
                if (prayerTimesResponse.isSuccessful) {
                    setPrayerAlarms(prayerTimesResponse.body())
                } else {
                    //todo check it later :/
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //todo check it later :/
            }
        }
    }

    private fun performDependencyInjection(context: Context?) {
        val component =
            DaggerPocketTreasureComponent.factory()
                .create(context as PocketTreasureApplication)

        treasureApi = component.getTreasureApi()
    }

    private fun setPrayerAlarms(prayerTimeResponse: PrayerTimeResponse?) {
        
    }
}