package com.stavro_xhardha.pockettreasure.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.stavro_xhardha.pockettreasure.brain.rescheduleMidnighReceiver

class AlarmRebootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED")) {
            rescheduleMidnighReceiver(context!!)
        }
    }
}