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
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.rocket.Rocket
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PrayerTimeNotificationReceiver : BroadcastReceiver() {

    private val CHANNEL_ID = "PrayerTimeId"
    @Inject
    lateinit var rocket: Rocket
    @Inject
    lateinit var picasso: Picasso

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        val title = intent?.getStringExtra(PRAYER_TITLE)
        val description = intent?.getStringExtra(PRAYER_DESCRIPTION)
        GlobalScope.launch {
            if (title.equals(FAJR) && rocket.readBoolean(NOTIFY_USER_FOR_FAJR)) {
                withContext(Dispatchers.Main) {
                    showPrayerNotification(context, title, description)
                }
            }
            if (title.equals(DHUHR) && rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)) {
                withContext(Dispatchers.Main) {
                    showPrayerNotification(context, title, description)
                }
            }
            if (title.equals(ASR) && rocket.readBoolean(NOTIFY_USER_FOR_ASR)) {
                withContext(Dispatchers.Main) {
                    showPrayerNotification(context, title, description)
                }
            }
            if (title.equals(MAGHRIB) && rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)) {
                withContext(Dispatchers.Main) {
                    showPrayerNotification(context, title, description)
                }
            }
            if (title.equals(ISHA) && rocket.readBoolean(NOTIFY_USER_FOR_ISHA)) {
                withContext(Dispatchers.Main) {
                    showPrayerNotification(context, title, description)
                }
            }
        }
    }

    private fun showPrayerNotification(
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
            .setColorized(true)
            .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setContentText(notificationDescription)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Prayer time notifications"
            val descriptionText = "This notifications notify when praying time has arrived"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }
    }
}