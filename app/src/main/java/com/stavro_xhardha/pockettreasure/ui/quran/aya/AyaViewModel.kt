package com.stavro_xhardha.pockettreasure.ui.quran.aya

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.brain.buildPagedList
import com.stavro_xhardha.pockettreasure.model.Aya
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import javax.inject.Inject

class AyaViewModel @Inject constructor(private val ayasDao: AyasDao) : ViewModel() {

    lateinit var ayas: LiveData<PagedList<Aya>>

    fun startSuraDataBaseCall(surahNumber: Int) {
        val listConfig = buildPagedList()
        val dataSourceFactory = ayasDao.getAyasBySurahNumber(surahNumber)
        ayas = LivePagedListBuilder(dataSourceFactory, listConfig).build()
    }
}