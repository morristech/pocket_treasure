package com.stavro_xhardha.pockettreasure

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.background.OfflinePrayerScheduler
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.rocket.Rocket
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import net.danlew.android.joda.JodaTimeAndroid
import org.joda.time.DateTime
import org.joda.time.DateTimeFieldType
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.IOException
import java.util.Calendar.*

@RunWith(JUnit4::class)
class OfflineSchedulerTest {
    private lateinit var offlinePrayerScheduler: OfflinePrayerScheduler
    private lateinit var rocket: Rocket
    private lateinit var context: Application
    private lateinit var prayerTimesDao: PrayerTimesDao

    @Before
    fun setUp() {
        context = mock()
        rocket = mock()
        prayerTimesDao = mock()
        offlinePrayerScheduler = OfflinePrayerScheduler(rocket, context, prayerTimesDao)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `on given empty string, getCurrentDayImplementation should return current day`() {
        val calendar = offlinePrayerScheduler.getCurrentDayPrayerImplementation("")

        assertEquals(calendar.dayOfMonth, 4)
    }
}