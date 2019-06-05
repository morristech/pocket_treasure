package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class NewsViewModelFactory @Inject constructor(
    val newsDataSourceFactory: NewsDataSourceFactory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        NewsViewModel(newsDataSourceFactory) as T
}