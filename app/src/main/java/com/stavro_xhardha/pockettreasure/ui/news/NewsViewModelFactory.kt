package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.rocket.Rocket
import javax.inject.Inject

class NewsViewModelFactory @Inject constructor(
    private val newsDataSourceFactory: NewsDataSourceFactory
    , private val rocket: Rocket
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NewsViewModel(newsDataSourceFactory, rocket) as T
}