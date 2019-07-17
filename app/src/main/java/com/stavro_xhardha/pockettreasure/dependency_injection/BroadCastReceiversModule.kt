package com.stavro_xhardha.pockettreasure.dependency_injection

import com.stavro_xhardha.pockettreasure.background.AlarmRebootReceiver
import com.stavro_xhardha.pockettreasure.background.MidnightScheduler
import com.stavro_xhardha.pockettreasure.background.PrayerTimeNotificationReceiver
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadCastReceiversModule {

    @ContributesAndroidInjector
    abstract fun contributesAlarmBootReceiver(): AlarmRebootReceiver

    @ContributesAndroidInjector
    abstract fun contributeMidnightScheduler(): MidnightScheduler

    @ContributesAndroidInjector
    abstract fun contributesPrayerTimeNotificationReceiver(): PrayerTimeNotificationReceiver
}