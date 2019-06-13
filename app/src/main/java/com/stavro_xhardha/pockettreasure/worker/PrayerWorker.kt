package com.stavro_xhardha.pockettreasure.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.*
import org.joda.time.LocalTime

internal class PrayerWorker(
    appContext: Context, workerParams: WorkerParameters,
    var treasureApi: TreasureApi,
    var rocket: Rocket
) : CoroutineWorker(appContext, workerParams) {


    override suspend fun doWork(): Result = coroutineScope {
        try {
            val prayerTimes = treasureApi.getPrayerTimesTodayAsync(
                rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY),
                rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY),
                1
            )
            if (prayerTimes.isSuccessful) {
                scheduleNotificationsForPrayerTimes(prayerTimes.body())
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun scheduleNotificationsForPrayerTimes(body: PrayerTimeResponse?) {
        body.let {
            Log.d(APPLICATION_TAG, "${System.currentTimeMillis()}")
            val currentLocalTime = LocalTime()
            val fajrTime = LocalTime(
                (it?.data?.timings?.fajr?.substring(0, 2))!!.toInt(),
                (it.data.timings.fajr.substring(3, 5)).toInt()
            )
            val dhuhrTime = LocalTime(
                (it.data.timings.dhuhr.substring(0, 2))!!.toInt(),
                (it.data.timings.dhuhr.substring(3, 5)).toInt()
            )
            val asrTime = LocalTime(
                (it.data.timings.asr.substring(0, 2))!!.toInt(),
                (it.data.timings.asr.substring(3, 5)).toInt()
            )
            val maghribTime = LocalTime(
                (it.data.timings.magrib.substring(0, 2))!!.toInt(),
                (it.data.timings.magrib.substring(3, 5)).toInt()
            )
            val ishaTime = LocalTime(
                (it.data.timings.isha.substring(0, 2))!!.toInt(),
                (it.data.timings.isha.substring(3, 5)).toInt()
            )
            if (currentLocalTime.isAfter(fajrTime)) {
                if (currentLocalTime.isAfter(dhuhrTime)) {
                    if (currentLocalTime.isAfter(asrTime)) {
                        if (currentLocalTime.isAfter(maghribTime)) {
                            if (currentLocalTime.isAfter(ishaTime)) {
                                //Do nothing
                                print(" DO NOTHING")
                            } else {
                                if (isNotificationDesired(NOTIFY_USER_FOR_ISHA))
                                    Log.d(APPLICATION_TAG, "Isha Scheduling.....")
                            }
                        } else {
                            if (isNotificationDesired(NOTIFY_USER_FOR_MAGHRIB))
                                Log.d(APPLICATION_TAG, "Maghrib Scheduling.....")
                            if (isNotificationDesired(NOTIFY_USER_FOR_ISHA))
                                Log.d(APPLICATION_TAG, "Isha Scheduling.....")
                        }
                    } else {
                        if (isNotificationDesired(NOTIFY_USER_FOR_ASR))
                            Log.d(APPLICATION_TAG, "ASR Scheduling.....")
                        if (isNotificationDesired(NOTIFY_USER_FOR_MAGHRIB))
                            Log.d(APPLICATION_TAG, "Maghrib Scheduling.....")
                        if (isNotificationDesired(NOTIFY_USER_FOR_ISHA))
                            Log.d(APPLICATION_TAG, "Isha Scheduling.....")
                    }
                } else {
                    if (isNotificationDesired(NOTIFY_USER_FOR_DHUHR))
                        Log.d(APPLICATION_TAG, "Dhuhr Scheduling.....")
                    if (isNotificationDesired(NOTIFY_USER_FOR_ASR))
                        Log.d(APPLICATION_TAG, "ASR Scheduling.....")
                    if (isNotificationDesired(NOTIFY_USER_FOR_MAGHRIB))
                        Log.d(APPLICATION_TAG, "Maghrib Scheduling.....")
                    if (isNotificationDesired(NOTIFY_USER_FOR_ISHA))
                        Log.d(APPLICATION_TAG, "Isha Scheduling.....")
                }
            } else {
                if (isNotificationDesired(NOTIFY_USER_FOR_FAJR))
                    Log.d(APPLICATION_TAG, "Fajr Scheduling.....")
                if (isNotificationDesired(NOTIFY_USER_FOR_DHUHR))
                    Log.d(APPLICATION_TAG, "Dhuhr Scheduling.....")
                if (isNotificationDesired(NOTIFY_USER_FOR_ASR))
                    Log.d(APPLICATION_TAG, "ASR Scheduling.....")
                if (isNotificationDesired(NOTIFY_USER_FOR_MAGHRIB))
                    Log.d(APPLICATION_TAG, "Maghrib Scheduling.....")
                if (isNotificationDesired(NOTIFY_USER_FOR_ISHA))
                    Log.d(APPLICATION_TAG, "Isha Scheduling.....")
            }
        }
    }

    private fun isNotificationDesired(notificationKey: String): Boolean = rocket.readBoolean(notificationKey)
}