package com.stavro_xhardha.pockettreasure.background

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
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.rocket.Rocket

class PrayerTimeNotificationReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "PrayerTimeId"
    private lateinit var rocket: Rocket

    override fun onReceive(context: Context?, intent: Intent?) {
        rocket = PocketTreasureApplication.getPocketTreasureComponent().getSharedPreferences()
        val title = intent?.getStringExtra(PRAYER_TITLE)
        val description = intent?.getStringExtra(PRAYER_DESCRIPTION)
        if (title.equals(FAJR) && rocket.readBoolean(NOTIFY_USER_FOR_FAJR))
            showSomeNotification(context, title, description)
        if (title.equals(DHUHR) && rocket.readBoolean(NOTIFY_USER_FOR_DHUHR))
            showSomeNotification(context, title, description)
        if (title.equals(ASR) && rocket.readBoolean(NOTIFY_USER_FOR_ASR))
            showSomeNotification(context, title, description)
        if (title.equals(MAGHRIB) && rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB))
            showSomeNotification(context, title, description)
        if (title.equals(ISHA) && rocket.readBoolean(NOTIFY_USER_FOR_ISHA))
            showSomeNotification(context, title, description)
    }

    private fun showSomeNotification(
        context: Context?,
        title: String?,
        notificationDescription: String?
    ) {
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