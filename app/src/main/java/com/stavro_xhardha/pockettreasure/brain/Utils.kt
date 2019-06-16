package com.stavro_xhardha.pockettreasure.brain

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.stavro_xhardha.pockettreasure.BuildConfig
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Aya
import com.stavro_xhardha.pockettreasure.model.News
import com.stavro_xhardha.pockettreasure.model.Surah
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import com.stavro_xhardha.pockettreasure.ui.setup.SetupViewModel
import java.util.*

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

fun <T> LiveData<T>.observeOnce(onChangeHandler: (T) -> Unit) {
    val observer = SetupViewModel.OneTimeObserver(handler = onChangeHandler)
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
        prayerTime.substring(4, 5)
    else
        prayerTime.substring(3, 5)
    set(Calendar.HOUR_OF_DAY, actualHour)
    set(Calendar.MINUTE, actualminute.toInt())
    set(Calendar.SECOND, 0)
}