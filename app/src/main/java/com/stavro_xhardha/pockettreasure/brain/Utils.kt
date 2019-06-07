package com.stavro_xhardha.pockettreasure.brain

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.BuildConfig
import com.stavro_xhardha.pockettreasure.R

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