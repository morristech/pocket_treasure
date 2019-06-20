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
import java.util.*

class PrayerMidnightReceiver : BroadcastReceiver() {

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
                    country
                )
                if (prayerTimesResponse.isSuccessful) {
                    setPrayerAlarms(prayerTimesResponse.body())
                } else {
                    //try in a quarter of hour
                    rescheduleMidnighReceiver(mContext, System.currentTimeMillis() + ((60 * 1000) * 16))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                //try in a quarter of hour
                rescheduleMidnighReceiver(mContext, System.currentTimeMillis() + ((60 * 1000) * 16))
            }
        }
    }

    private fun giveValuesToDependencies() {
        val appComponent = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = appComponent.getTreasureApi()
        rocket = appComponent.getSharedPreferences()
    }

    private fun setPrayerAlarms(prayerTimeResponse: PrayerTimeResponse?) {
        val currentTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        prayerTimeResponse.let {
            if (rocket.readBoolean(NOTIFY_USER_FOR_FAJR)) {
                if (currentTime.before(getCurrentDayPrayerImplementation(it?.data?.timings?.fajr!!)))
                    scheduleAlarm(
                        mContext,
                        getCurrentDayPrayerImplementation(it.data.timings.fajr),
                        PENDING_INTENT_FIRE_NOTIFICATION_FAJR,
                        PrayerTimeAlarm::class.java
                    )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                if (currentTime.before(getCurrentDayPrayerImplementation(it?.data?.timings?.dhuhr!!)))
                    scheduleAlarm(
                        mContext,
                        getCurrentDayPrayerImplementation(it.data.timings.dhuhr),
                        PENDING_INTENT_FIRE_NOTIFICATION_DHUHR,
                        PrayerTimeAlarm::class.java
                    )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_ASR)) {
                if (currentTime.before(getCurrentDayPrayerImplementation(it?.data?.timings?.asr!!)))
                    scheduleAlarm(
                        mContext,
                        getCurrentDayPrayerImplementation(it.data.timings.asr),
                        PENDING_INTENT_FIRE_NOTIFICATION_ASR,
                        PrayerTimeAlarm::class.java
                    )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)) {
                if (currentTime.before(getCurrentDayPrayerImplementation(it?.data?.timings?.magrib!!)))
                    scheduleAlarm(
                        mContext,
                        getCurrentDayPrayerImplementation(it.data.timings.magrib),
                        PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB,
                        PrayerTimeAlarm::class.java
                    )
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_ISHA)) {
                if (currentTime.before(getCurrentDayPrayerImplementation(it?.data?.timings?.isha!!)))
                    scheduleAlarm(
                        mContext,
                        getCurrentDayPrayerImplementation(it.data.timings.isha),
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
            PrayerMidnightReceiver::class.java
        )
    }
}