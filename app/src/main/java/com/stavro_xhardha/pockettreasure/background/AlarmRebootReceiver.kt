package com.stavro_xhardha.pockettreasure.background

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG
import com.stavro_xhardha.pockettreasure.brain.isDebugMode
import com.stavro_xhardha.pockettreasure.brain.startSchedulingPrayerTimeNotifications

class AlarmRebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            startSchedulingPrayerTimeNotifications(context!!)
            if (isDebugMode)
                Log.d(APPLICATION_TAG , "WorksAfter Reboot")
        }
    }
}