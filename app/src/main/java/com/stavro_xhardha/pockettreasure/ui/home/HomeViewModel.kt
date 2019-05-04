package com.stavro_xhardha.pockettreasure.ui.home

import androidx.lifecycle.ViewModel
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository,
    coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
}