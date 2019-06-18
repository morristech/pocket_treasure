package com.stavro_xhardha.pockettreasure.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.PRAYER_DESCRIPTION
import com.stavro_xhardha.pockettreasure.brain.PRAYER_TITLE

class PrayerTimeAlarm : BroadcastReceiver() {

    private val CHANNEL_ID = "Some Channel Id"

    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra(PRAYER_TITLE)
        val description = intent?.getStringExtra(PRAYER_DESCRIPTION)
        showSomeNotification(context, title, description)
    }

    private fun showSomeNotification(
        context: Context?,
        title: String?,
        notificationDescription: String?
    ) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        val builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_home_grey_24dp)
            .setContentTitle(title)
            .setContentText(notificationDescription)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Much longer text that cannot fit one line...")
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = CHANNEL_ID
            val descriptionText = "SomeId"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}