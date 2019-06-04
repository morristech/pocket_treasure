package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(val newsRepository: NewsRepository): ViewModel() {

    init {
        loadLatestNews()
    }

    private fun loadLatestNews() {
        viewModelScope.launch(Dispatchers.IO) {
            val news = newsRepository.callLatestNewsAsync()
        }
    }
}