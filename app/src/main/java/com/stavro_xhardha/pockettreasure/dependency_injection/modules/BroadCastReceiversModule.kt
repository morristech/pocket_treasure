package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import com.stavro_xhardha.pockettreasure.background.AlarmRebootReceiver
import com.stavro_xhardha.pockettreasure.background.MidnightScheduler
import com.stavro_xhardha.pockettreasure.background.PrayerTimeNotificationReceiver
import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.BackgroundScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BroadCastReceiversModule {

    @BackgroundScope
    @ContributesAndroidInjector
    abstract fun contributesAlarmBootReceiver(): AlarmRebootReceiver

    @BackgroundScope
    @ContributesAndroidInjector
    abstract fun contributeMidnightScheduler(): MidnightScheduler

    @BackgroundScope
    @ContributesAndroidInjector
    abstract fun contributesPrayerTimeNotificationReceiver(): PrayerTimeNotificationReceiver
}