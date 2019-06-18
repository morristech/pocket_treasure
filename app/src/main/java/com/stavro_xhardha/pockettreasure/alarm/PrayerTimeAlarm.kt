package com.stavro_xhardha.pockettreasure.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.PENDING_INTENT_FIRE_MAIN_ACTIVITY
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

        val resultIntent = Intent(context, MainActivity::class.java)
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context!!).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(PENDING_INTENT_FIRE_MAIN_ACTIVITY, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentIntent(resultPendingIntent)
            .setSmallIcon(R.drawable.ic_mosque_small)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(notificationDescription)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "prayer_time_name"
            val descriptionText = "prayer_time_description"
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