package com.stavro_xhardha.pockettreasure.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerMonthDays
import com.stavro_xhardha.pockettreasure.model.PrayerTimeYearResponse
import com.stavro_xhardha.pockettreasure.model.PrayerTiming
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.LocalDate

class PrayerTimeWorkManager(val context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {
    private lateinit var treasureDatabase: TreasureDatabase
    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket
    private lateinit var prayerTimesDao: PrayerTimesDao

    override suspend fun doWork(): Result = coroutineScope {
        instantiateData()
        launch {
            val yearPrayerTimes = treasureApi.getAllYearsPrayerTImesAsync(
                rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY),
                rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)
            )
            if (yearPrayerTimes.isSuccessful) {
                handleResponse(yearPrayerTimes.body())
                Result.success()
            } else {
                Result.failure()
            }
        }
        Result.success()
    }

    private suspend fun handleResponse(body: PrayerTimeYearResponse?) {
        reviewCurrentMonth(body?.data?.january!!)
        reviewCurrentMonth(body.data.february)
        reviewCurrentMonth(body.data.marches)
        reviewCurrentMonth(body.data.april)
        reviewCurrentMonth(body.data.may)
        reviewCurrentMonth(body.data.june)
        reviewCurrentMonth(body.data.july)
        reviewCurrentMonth(body.data.august)
        reviewCurrentMonth(body.data.september)
        reviewCurrentMonth(body.data.october)
        reviewCurrentMonth(body.data.november)
        reviewCurrentMonth(body.data.december)

        if (isDebugMode) {
            val selection = prayerTimesDao.selectAll()
            selection.forEach {
                Log.d(APPLICATION_TAG, " DATA INSERTED : ${it.id}")
            }
        }

        withContext(Dispatchers.IO) {
            startSchedulingPrayerTimeNotifications(context)
        }
    }

    private suspend fun reviewCurrentMonth(monthDays: List<PrayerMonthDays>) {
        val currentDateTime = DateTime()
        monthDays.forEach {
            val incomingTime = DateTime(it.prayerDate.timestamp.toLong() * 1000L)
            if (incomingTime.isAfter(currentDateTime) || (incomingTime.toLocalDate()).equals(LocalDate())) {
                prayerTimesDao.insertPrayerTimes(
                    PrayerTiming(
                        id = 0,
                        fajr = it.timing.fajr,
                        sunrise = it.timing.sunrise,
                        dhuhr = it.timing.dhuhr,
                        asr = it.timing.asr,
                        sunset = it.timing.sunset,
                        magrib = it.timing.magrib,
                        isha = it.timing.isha,
                        midnight = it.timing.midnight,
                        imsak = it.timing.imsak,
                        isFired = 0,
                        timestamp = it.prayerDate.timestamp.toLong() * 1000L
                    )
                )
            }
        }
    }

    private fun instantiateData() {
        val application = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = application.getTreasureApi()
        rocket = application.getSharedPreferences()
        treasureDatabase = application.treasureDatabase()
        prayerTimesDao = treasureDatabase.prayerTimesDao()
    }
}