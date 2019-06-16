package com.stavro_xhardha.pockettreasure

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
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

        Log.d(APPLICATION_TAG, "Is it null? $treasureApi")
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

                }
            } catch (e: Exception) {
                e.printStackTrace()
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
                setAlarm(prayerTimeResponse?.data?.timings?.fajr!!)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.dhuhr!!)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.asr!!)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.magrib!!)
            }
            if (rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                setAlarm(prayerTimeResponse?.data?.timings?.isha!!)
            }
        }
    }

    private fun setAlarm(prayerTime: String) {
        val prayerTime = getCurrentDayPrayerImplementation(prayerTime)

        val intent = Intent(mContext, PrayerTimeAlarm::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(mContext, PENDING_INTENT_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setExact(
            AlarmManager.RTC,
            prayerTime.timeInMillis,
            pendingIntent
        )
    }
}