package com.stavro_xhardha.pockettreasure.brain

import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.BuildConfig

val isDebugMode: Boolean = BuildConfig.DEBUG

fun buildPagedList() = PagedList.Config.Builder()
    .setPageSize(INITIAL_PAGE_SIZE)
    .setEnablePlaceholders(false)
    .build()