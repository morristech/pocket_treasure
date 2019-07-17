package com.stavro_xhardha.pockettreasure.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stavro_xhardha.PocketTreasureApplication
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MidnightScheduler : BroadcastReceiver() {

    @Inject
    lateinit var offlinePrayerScheduler: OfflinePrayerScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        //it's midnight
        AndroidInjection.inject(this, context)
        GlobalScope.launch(Dispatchers.IO) {
            offlinePrayerScheduler.initScheduler()
        }
    }
}