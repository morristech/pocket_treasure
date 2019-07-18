package com.stavro_xhardha

import android.app.Activity
import android.app.Application
import android.content.BroadcastReceiver
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkManager.*
import com.stavro_xhardha.pockettreasure.dependency_injection.DaggerPocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.PrayerWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Inject

class PocketTreasureApplication : Application(), HasActivityInjector, HasBroadcastReceiverInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>
    @Inject
    lateinit var dispatchingReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>
    @Inject
    lateinit var workerFactory: PrayerWorkerFactory

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        DaggerPocketTreasureComponent.factory().create(this).inject(this)

        initialize(this, Configuration.Builder().setWorkerFactory(workerFactory).build())
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingAndroidInjector

    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = dispatchingReceiverInjector
}