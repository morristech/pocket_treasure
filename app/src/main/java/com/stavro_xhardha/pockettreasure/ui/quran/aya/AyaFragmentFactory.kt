package com.stavro_xhardha.pockettreasure.ui.quran.aya

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import javax.inject.Inject

class AyaFragmentFactory(private val ayasDao: AyasDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = AyaViewModel(ayasDao) as T
}