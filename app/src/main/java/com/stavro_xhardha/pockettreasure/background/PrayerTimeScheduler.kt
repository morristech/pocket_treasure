package com.stavro_xhardha.pockettreasure.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import com.stavro_xhardha.pockettreasure.model.PrayerTiming
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.rocket.Rocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import java.util.Calendar.*

class PrayerTimeScheduler : BroadcastReceiver() {

    private lateinit var treasureApi: TreasureApi
    private lateinit var rocket: Rocket
    private lateinit var mContext: Context
    private lateinit var prayerTimeDao: PrayerTimesDao

    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context!!
        initializeDependencies()
        //it's midnight
        GlobalScope.launch(Dispatchers.IO) {
            val date = DateTime()
            val calendar = getInstance().apply {
                set(YEAR, date.year)
                set(MONTH, date.monthOfYear - 1)
                set(DAY_OF_MONTH, date.dayOfMonth)
                set(HOUR_OF_DAY, 9)
                set(MINUTE, 1)
                set(SECOND, 1)
                set(MILLISECOND, 0)
            }
            val todaysSelection = prayerTimeDao.selectAllPrayersWhereDateAndIsFired(calendar.timeInMillis, 0)
            if (todaysSelection.isNotEmpty()) {
                todaysSelection.forEach {
                    scheduleNotificationTimes(it)
                }
                invokeTomorowAlarm()
            } else {
                startWorkManager()
                Toast.makeText(context, "YOU ARE DOING SOMETHING WRONG", Toast.LENGTH_LONG).show()
                //January 1
            }
        }
    }

    private fun scheduleNotificationTimes(it: PrayerTiming) {
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_FAJR),
            it.fajr,
            PENDING_INTENT_FIRE_NOTIFICATION_FAJR
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_DHUHR),
            it.dhuhr,
            PENDING_INTENT_FIRE_NOTIFICATION_DHUHR
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_ASR),
            it.asr,
            PENDING_INTENT_FIRE_NOTIFICATION_ASR
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB),
            it.magrib,
            PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB
        )
        scheduleAlarmForPrayer(
            rocket.readBoolean(NOTIFY_USER_FOR_ISHA),
            it.isha,
            PENDING_INTENT_FIRE_NOTIFICATION_ISHA
        )
    }

    private fun initializeDependencies() {
        val appComponent = PocketTreasureApplication.getPocketTreasureComponent()
        treasureApi = appComponent.getTreasureApi()
        rocket = appComponent.getSharedPreferences()
        prayerTimeDao = appComponent.treasureDatabase().prayerTimesDao()
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
        val currentTime = getInstance().apply {
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
            if (isDebugMode)
                Log.d(APPLICATION_TAG, "ALARM SET IN: $prayerTime")
        }
    }

    private fun invokeTomorowAlarm() {
        val midnight = getInstance().apply {
            add(DAY_OF_MONTH, 1)
            set(HOUR_OF_DAY, 0)
            set(MINUTE, 0)
            set(SECOND, 1)
        }
        schedulePrayingAlarm(
            mContext,
            midnight,
            PENDING_INTENT_SYNC,
            PrayerTimeScheduler::class.java
        )

        if (isDebugMode)
            Log.d(
                APPLICATION_TAG, " ALARM SET ALSO IN: DAY: ${midnight.get(DAY_OF_MONTH)}, HR: ${midnight.get(
                    HOUR_OF_DAY
                )}, Minute: ${midnight.get(MINUTE)} and Second: ${midnight.get(SECOND)} "
            )
    }
}