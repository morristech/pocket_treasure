package com.stavro_xhardha.pockettreasure

import android.app.Application
import android.content.Context
import android.content.Intent
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.stavro_xhardha.pockettreasure.brain.CAPITAL_SHARED_PREFERENCES_KEY
import com.stavro_xhardha.pockettreasure.brain.COUNTRY_SHARED_PREFERENCE_KEY
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class PrayerAlarmReceiverTest {

    private lateinit var prayerAlarmReceiver: PrayerAlarmReceiver
    private lateinit var treasureApi: TreasureApi
    private lateinit var context: Context
    private lateinit var intent: Intent

    @Before
    fun setUp() {
        context = mock(Application::class.java)
        intent = mock(Intent::class.java)
        treasureApi = mock()
        prayerAlarmReceiver = PrayerAlarmReceiver()
    }

    @After
    fun tearDown() {
        print("TEST HAS BEEN FINISHED")
    }

    @Test
    fun `prayerAlarmReceiver check if prayer method gets called`() {
        prayerAlarmReceiver.onReceive(context, intent)
        runBlocking {
            verify(
                treasureApi.getPrayerTimesTodayAsync(
                    CAPITAL_SHARED_PREFERENCES_KEY,
                    COUNTRY_SHARED_PREFERENCE_KEY,
                    1
                )
            )
        }
    }
}