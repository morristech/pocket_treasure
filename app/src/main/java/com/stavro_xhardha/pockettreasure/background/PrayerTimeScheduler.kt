package com.stavro_xhardha.pockettreasure.background

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

class PrayerTimeScheduler : BroadcastReceiver() {

    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket
    private lateinit var mContext: Context

    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context!!
        initializeDependencies()
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
                    invokeTomorowAlarm(prayerTimesResponse.body()!!.data.timings.midnight)
                } else {
                    startSchedulingPrayerTimeNotifications(mContext, System.currentTimeMillis() + ((60 * 1000) * 16))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                startSchedulingPrayerTimeNotifications(mContext, System.currentTimeMillis() + ((60 * 1000) * 16))
            }
        }
    }

    private fun initializeDependencies() {
        val appComponent = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = appComponent.getTreasureApi()
        rocket = appComponent.getSharedPreferences()
    }

    private fun setPrayerAlarms(prayerTimeResponse: PrayerTimeResponse?) {
        prayerTimeResponse.let {
            scheduleAlarmForPrayer(
                rocket.readBoolean(NOTIFY_USER_FOR_FAJR),
                it?.data?.timings?.fajr!!,
                PENDING_INTENT_FIRE_NOTIFICATION_FAJR
            )
            scheduleAlarmForPrayer(
                rocket.readBoolean(NOTIFY_USER_FOR_DHUHR),
                it.data.timings.dhuhr,
                PENDING_INTENT_FIRE_NOTIFICATION_DHUHR
            )
            scheduleAlarmForPrayer(
                rocket.readBoolean(NOTIFY_USER_FOR_ASR),
                it.data.timings.asr,
                PENDING_INTENT_FIRE_NOTIFICATION_ASR
            )
            scheduleAlarmForPrayer(
                rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB),
                it.data.timings.magrib,
                PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB
            )
            scheduleAlarmForPrayer(
                rocket.readBoolean(NOTIFY_USER_FOR_ISHA),
                it.data.timings.isha,
                PENDING_INTENT_FIRE_NOTIFICATION_ISHA
            )
        }
    }

    private fun scheduleAlarmForPrayer(
        isNotificationTrigable: Boolean,
        prayerTime: String,
        pendingIntentKey: Int
    ) {
        val currentTime = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }
        if (isNotificationTrigable) {
            if (currentTime.before(getCurrentDayPrayerImplementation(prayerTime))) {
                schedulePrayingAlarm(
                    mContext,
                    getCurrentDayPrayerImplementation(prayerTime),
                    pendingIntentKey,
                    PrayerTimeAlarm::class.java
                )
            }
        }
    }

    private fun invokeTomorowAlarm(midnight: String) {
        schedulePrayingAlarm(
            mContext,
            getMidnightImplementation(midnight),
            PENDING_INTENT_SYNC,
            PrayerTimeScheduler::class.java
        )
    }
}