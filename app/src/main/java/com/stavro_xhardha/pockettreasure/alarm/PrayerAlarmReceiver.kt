package com.stavro_xhardha.pockettreasure.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PrayerAlarmReceiver : BroadcastReceiver() {

    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket
    private lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context!!
        giveValuesToDependencies()
        //it's midnight
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val city = rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)
                val country = rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)
                val prayerTimesResponse = treasureApi.getPrayerTimesTodayAsync(
                    city,
                    country, 1
                )
                if (prayerTimesResponse.isSuccessful) {
                    setPrayerAlarms(prayerTimesResponse.body())
                } else {
                    scheduleAlarmAfterOneHour(mContext)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                scheduleAlarmAfterOneHour(mContext)
            }
        }
    }

    private fun giveValuesToDependencies() {
        val appComponent = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = appComponent.getTreasureApi()
        rocket = appComponent.getSharedPreferences()
    }

    private fun setPrayerAlarms(prayerTimeResponse: PrayerTimeResponse?) {
        prayerTimeResponse.let {
            if (rocket.readBoolean(NOTIFY_USER_FOR_FAJR)) {
                scheduleAlarm(
                    mContext,
                    getCurrentDayPrayerImplementation(prayerTimeResponse?.data?.timings?.fajr!!),
                    PENDING_INTENT_FIRE_NOTIFICATION_FAJR,
                    PrayerTimeAlarm::class.java
                )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                scheduleAlarm(
                    mContext,
                    getCurrentDayPrayerImplementation(prayerTimeResponse?.data?.timings?.fajr!!),
                    PENDING_INTENT_FIRE_NOTIFICATION_DHUHR,
                    PrayerTimeAlarm::class.java
                )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_ASR)) {
                scheduleAlarm(
                    mContext,
                    getCurrentDayPrayerImplementation(prayerTimeResponse?.data?.timings?.fajr!!),
                    PENDING_INTENT_FIRE_NOTIFICATION_ASR,
                    PrayerTimeAlarm::class.java
                )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)) {
                scheduleAlarm(
                    mContext,
                    getCurrentDayPrayerImplementation(prayerTimeResponse?.data?.timings?.fajr!!),
                    PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB,
                    PrayerTimeAlarm::class.java
                )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_ISHA)) {
                scheduleAlarm(
                    mContext,
                    getCurrentDayPrayerImplementation(prayerTimeResponse?.data?.timings?.fajr!!),
                    PENDING_INTENT_FIRE_NOTIFICATION_ISHA,
                    PrayerTimeAlarm::class.java
                )
            }

            invokeTomorowAlarm(prayerTimeResponse!!.data.timings.midnight)
        }
    }

    private fun invokeTomorowAlarm(midnight: String) {
        scheduleAlarm(
            mContext,
            getMidnightImplementation(midnight),
            PENDING_INTENT_SYNC,
            PrayerTimeAlarm::class.java
        )
    }
}