package com.stavro_xhardha.pockettreasure.brain

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import androidx.test.espresso.IdlingResource
import com.stavro_xhardha.pockettreasure.BuildConfig
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.alarm.PrayerAlarmReceiver
import com.stavro_xhardha.pockettreasure.model.*
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

val isDebugMode: Boolean = BuildConfig.DEBUG

fun buildPagedList() = PagedList.Config.Builder()
    .setPageSize(INITIAL_PAGE_SIZE)
    .setEnablePlaceholders(false)
    .build()

fun getBackToHomeFragment(
    view: View,
    mActivity: FragmentActivity,
    fragment: Fragment
) {
    val navController = Navigation.findNavController(view)
    mActivity.onBackPressedDispatcher.addCallback(fragment, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.homeFragment, false)
        }
    })
}

val DIFF_UTIL_AYA = object : DiffUtil.ItemCallback<Aya>() {
    override fun areItemsTheSame(oldItem: Aya, newItem: Aya): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Aya, newItem: Aya): Boolean =
        oldItem.ayatText == newItem.ayatText
                && oldItem.audioUrl == newItem.audioUrl
                && oldItem.id == newItem.id

}

val DIFF_UTIL_GALLERY = object : DiffUtil.ItemCallback<UnsplashResult>() {
    override fun areItemsTheSame(oldItem: UnsplashResult, newItem: UnsplashResult): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: UnsplashResult, newItem: UnsplashResult): Boolean =
        oldItem.id == newItem.id && oldItem.description == newItem.description
                && oldItem.altDescription == newItem.description
                && oldItem.photoUrls == newItem.photoUrls
}

val DIFF_UTIL_NEWS = object : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean = oldItem.title == newItem.title

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.title == newItem.title
                && oldItem.author == newItem.author
                && oldItem.content == newItem.content
                && oldItem.urlOfImage == newItem.urlOfImage
                && oldItem.description == newItem.description
    }
}

val DIFF_UTIL_QURAN = object : DiffUtil.ItemCallback<Surah>() {
    override fun areItemsTheSame(oldItem: Surah, newItem: Surah): Boolean = oldItem.surahNumber == newItem.surahNumber

    override fun areContentsTheSame(oldItem: Surah, newItem: Surah): Boolean =
        oldItem.englishName == newItem.englishName
                && oldItem.englishTranslation == newItem.englishTranslation
                && oldItem.surahArabicName == newItem.surahArabicName
                && oldItem.revelationType == newItem.revelationType
                && oldItem.surahNumber == newItem.surahNumber

}

val DIFF_UTIL_TASBEEH = object : DiffUtil.ItemCallback<Tasbeeh>() {
    override fun areItemsTheSame(oldItem: Tasbeeh, newItem: Tasbeeh): Boolean =
        oldItem.arabicPhrase == newItem.arabicPhrase

    override fun areContentsTheSame(oldItem: Tasbeeh, newItem: Tasbeeh): Boolean =
        oldItem.arabicPhrase == newItem.arabicPhrase
                && oldItem.translation == newItem.translation
                && oldItem.transliteration == newItem.transliteration

}

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = OneTimeObserver(handler = onChangeHandler)
    observe(observer, observer)
}

fun getDefaultMidnightImplementation(): Calendar = Calendar.getInstance().apply {
    add(Calendar.DATE, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 30)
    set(Calendar.SECOND, 0)
}

fun getMidnightImplementation(midnightInput: String): Calendar = Calendar.getInstance().apply {
    val actualHour = if (midnightInput.startsWith("0"))
        midnightInput.substring(1, 2).toInt()
    else
        midnightInput.substring(0, 2).toInt()

    val actualminute = if (midnightInput.substring(3, 5).startsWith("0"))
        midnightInput.substring(4, 5)
    else
        midnightInput.substring(3, 5)
    add(Calendar.DATE, 1)
    set(Calendar.HOUR_OF_DAY, actualHour)
    set(Calendar.MINUTE, actualminute.toInt())
    set(Calendar.SECOND, 0)
}

fun getCurrentDayPrayerImplementation(prayerTime: String): Calendar = Calendar.getInstance().apply {
    val actualHour = if (prayerTime.startsWith("0"))
        prayerTime.substring(1, 2).toInt()
    else
        prayerTime.substring(0, 2).toInt()

    val actualminute = if (prayerTime.substring(3, 5).startsWith("0"))
        prayerTime.substring(4, 5).toInt()
    else
        prayerTime.substring(3, 5).toInt()
    set(Calendar.HOUR_OF_DAY, actualHour)
    set(Calendar.MINUTE, actualminute)
    set(Calendar.SECOND, 0)
}

fun scheduleAlarm(
    time: Calendar,
    alarmManager: AlarmManager,
    pendingIntent: PendingIntent
) {
    alarmManager.setExact(
        AlarmManager.RTC,
        time.timeInMillis,
        pendingIntent
    )
}

fun scheduleAlarm(mContext: Context, time: Calendar, pendingIntentKey: Int, desiredClass: Class<*>) {
    val intent = Intent(mContext, desiredClass)
    checkIntentVariables(pendingIntentKey, intent)
    val pendingIntent =
        PendingIntent.getBroadcast(
            mContext,
            pendingIntentKey,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    scheduleAlarm(time, alarmManager, pendingIntent)

    if (isDebugMode)
        Log.d(APPLICATION_TAG, "Alarm set at ${time.timeInMillis}")
}

fun checkIntentVariables(intentKey: Int, intent: Intent) {
    when (intentKey) {
        PENDING_INTENT_FIRE_NOTIFICATION_FAJR -> {
            intent.putExtra(PRAYER_TITLE, "Fajr")
            intent.putExtra(PRAYER_DESCRIPTION, "Fajr time has arrived")
        }
        PENDING_INTENT_FIRE_NOTIFICATION_DHUHR -> {
            intent.putExtra(PRAYER_TITLE, "Dhuhr")
            intent.putExtra(PRAYER_DESCRIPTION, "Dhuhr time has arrived")
        }
        PENDING_INTENT_FIRE_NOTIFICATION_ASR -> {
            intent.putExtra(PRAYER_TITLE, "Asr")
            intent.putExtra(PRAYER_DESCRIPTION, "Asr time has arrived")
        }
        PENDING_INTENT_FIRE_NOTIFICATION_MAGHRIB -> {
            intent.putExtra(PRAYER_TITLE, "Maghrib")
            intent.putExtra(PRAYER_DESCRIPTION, "Maghrib time has arrived")
        }
        PENDING_INTENT_FIRE_NOTIFICATION_ISHA -> {
            intent.putExtra(PRAYER_TITLE, "Isha")
            intent.putExtra(PRAYER_DESCRIPTION, "Isha time has arrived")
        }
    }
}

fun scheduleAlarmAfterOneHour(context: Context) {
    val intent = Intent(context, PrayerAlarmReceiver::class.java)
    val pendingIntent =
        PendingIntent.getBroadcast(context, PENDING_INTENT_SYNC, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    alarmManager.setExact(
        AlarmManager.RTC,
        System.currentTimeMillis() + ((60 * 1000) * 60),
        pendingIntent
    )
}

class SmoothieThermometer(private val resourceName: String) : IdlingResource {

    private val counter = AtomicInteger(0)

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(resourceCallback: IdlingResource.ResourceCallback) {
        this.resourceCallback = resourceCallback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterVal = counter.decrementAndGet()
        if (counterVal == 0) {
            resourceCallback?.onTransitionToIdle()
        } else if (counterVal < 0) {
            throw IllegalStateException("Your counter has been used wrong")
        }
    }

}

object Smoothie {
    private const val RESOURCE = "SMOOTHIE"

    @JvmField
    val countingIdlingResource = SmoothieThermometer(RESOURCE)

    fun startProcess() {
        countingIdlingResource.increment()
    }

    fun endProcess() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}