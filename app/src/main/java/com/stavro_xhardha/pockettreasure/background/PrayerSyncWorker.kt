package com.stavro_xhardha.pockettreasure.background

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.dependency_injection.ChildWorkerFactory
import com.stavro_xhardha.pockettreasure.model.PrayerMonthDays
import com.stavro_xhardha.pockettreasure.model.PrayerTimeYearResponse
import com.stavro_xhardha.pockettreasure.model.PrayerTiming
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import dagger.android.AndroidInjection
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider

class PrayerSyncWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted parameters: WorkerParameters,
    val treasureDatabase: TreasureDatabase,
    val treasureApi: TreasureApi,
    val rocket: Rocket,
    val prayerTimesDao: PrayerTimesDao,
    val offlinePrayerScheduler: OfflinePrayerScheduler
) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result = coroutineScope {
        instantiateDependencies()
        launch {
            val yearPrayerTimes = treasureApi.getAllYearsPrayerTImesAsync(
                rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY),
                rocket.readString(COUNTRY_SHARED_PREFERENCE_KEY)
            )
            if (yearPrayerTimes.isSuccessful) {
                handleResponse(yearPrayerTimes.body())
                Result.success()
            } else {
                Result.retry()
            }
        }
        Result.success()
    }

    private suspend fun handleResponse(body: PrayerTimeYearResponse?) {
        reviewCurrentMonth(
            body?.data?.january!!,
            body.data.february,
            body.data.march,
            body.data.april,
            body.data.may,
            body.data.june,
            body.data.july,
            body.data.august,
            body.data.september,
            body.data.october,
            body.data.november,
            body.data.december
        )

        if (isDebugMode) {
            val selection = prayerTimesDao.selectAll()
            selection.forEach {
                Log.d(APPLICATION_TAG, " DATA INSERTED : ${it.id} and size of selection is: ${selection.size}")
            }
        }

        offlinePrayerScheduler.initScheduler()
        rocket.writeBoolean(DATA_ARE_READY, true)
    }

    private suspend fun reviewCurrentMonth(vararg monthDays: List<PrayerMonthDays>) {
        val currentDateTime = DateTime()
        monthDays.forEach {
            it.forEach { prayerMonthDays ->
                val incomingTime = DateTime(prayerMonthDays.prayerDate.timestamp.toLong() * 1000L)
                if (incomingTime.isAfter(currentDateTime) || (incomingTime.toLocalDate()).equals(LocalDate())) {
                    prayerTimesDao.insertPrayerTimes(
                        PrayerTiming(
                            id = 0,
                            fajr = prayerMonthDays.timing.fajr,
                            sunrise = prayerMonthDays.timing.sunrise,
                            dhuhr = prayerMonthDays.timing.dhuhr,
                            asr = prayerMonthDays.timing.asr,
                            sunset = prayerMonthDays.timing.sunset,
                            magrib = prayerMonthDays.timing.magrib,
                            isha = prayerMonthDays.timing.isha,
                            midnight = prayerMonthDays.timing.midnight,
                            imsak = prayerMonthDays.timing.imsak,
                            isFired = 0,
                            timestamp = prayerMonthDays.prayerDate.timestamp.toLong() * 1000L
                        )
                    )
                }
            }
        }
    }

    private fun instantiateDependencies() {
//        val application = PocketTreasureApplication.getPocketTreasureComponent()
//        treasureApi = application.getTreasureApi()
//        rocket = application.getSharedPreferences()
//        treasureDatabase = application.treasureDatabase()
//        prayerTimesDao = treasureDatabase.prayerTimesDao()
//        offlineScheduler = application.offlineScheduler()
    }

    @AssistedInject.Factory
    interface PrayerFactory : ChildWorkerFactory
}