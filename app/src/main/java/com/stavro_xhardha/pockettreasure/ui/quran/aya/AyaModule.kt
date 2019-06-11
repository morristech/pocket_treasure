package com.stavro_xhardha.pockettreasure.ui.quran.aya

import com.stavro_xhardha.pockettreasure.dependency_injection.QuranSubScope
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import dagger.Module
import dagger.Provides

@Module
class AyaModule {

    @Provides
    @QuranSubScope
    fun provideAyasFragmentFactory(ayasDao: AyasDao): AyaFragmentFactory = AyaFragmentFactory(ayasDao)

}