package com.stavro_xhardha.pockettreasure.ui.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class QuranFragmentFactory @Inject constructor(private val quranRepository: QuranRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = QuranViewModel(quranRepository) as T
}