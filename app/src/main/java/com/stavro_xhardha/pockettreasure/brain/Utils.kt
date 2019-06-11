package com.stavro_xhardha.pockettreasure.brain

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.DiffUtil
import com.stavro_xhardha.pockettreasure.BuildConfig
import com.stavro_xhardha.pockettreasure.R
import com.stavro_xhardha.pockettreasure.model.Aya
import com.stavro_xhardha.pockettreasure.model.News
import com.stavro_xhardha.pockettreasure.model.Surah
import com.stavro_xhardha.pockettreasure.model.UnsplashResult

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