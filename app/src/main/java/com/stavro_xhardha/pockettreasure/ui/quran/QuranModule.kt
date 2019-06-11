package com.stavro_xhardha.pockettreasure.ui.quran

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import com.stavro_xhardha.pockettreasure.room_db.SurahsDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.ui.quran.aya.AyaComponent
import dagger.Module
import dagger.Provides

@Module
class QuranModule {

    @Provides
    @FragmentScope
    fun provideSurahsDao(treasureDatabase: TreasureDatabase): SurahsDao = treasureDatabase.surahsDao()

    @Provides
    @FragmentScope
    fun provideAyasDao(treasureDatabase: TreasureDatabase): AyasDao = treasureDatabase.ayasDao()

    @Provides
    @FragmentScope
    fun provideQuranFragmentFactory(quranRepository: QuranRepository): QuranFragmentFactory =
        QuranFragmentFactory(quranRepository)

    @Provides
    @FragmentScope
    fun provideQuranRepository(treasureApi: TreasureApi, surahsDao: SurahsDao, ayasDao: AyasDao): QuranRepository =
        QuranRepository(treasureApi, surahsDao, ayasDao)
}