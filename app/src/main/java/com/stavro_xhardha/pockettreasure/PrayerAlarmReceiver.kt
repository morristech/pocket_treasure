package com.stavro_xhardha.pockettreasure

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.crashlytics.android.Crashlytics
import com.stavro_xhardha.pockettreasure.brain.APPLICATION_TAG

class PrayerAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Crashlytics.log("ALARM RECEIVED at ${System.currentTimeMillis()}")
        Log.d(APPLICATION_TAG, "ALARM RECEIVED at ${System.currentTimeMillis()}")

        Toast.makeText(context, "ESSELAMU ALEJKUM WA RAHMETULLAHI WA BEREKATUHU", Toast.LENGTH_LONG).show()
    }
}