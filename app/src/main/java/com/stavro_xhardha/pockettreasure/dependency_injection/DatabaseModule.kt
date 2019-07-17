package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.pockettreasure.brain.TREASURE_DATABASE_NAME
import com.stavro_xhardha.pockettreasure.room_db.*
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {

    @Provides
    @ApplicationScope
    fun provideRoomDatabase(context: Application): TreasureDatabase = Room.databaseBuilder(
        context,
        TreasureDatabase::class.java, TREASURE_DATABASE_NAME
    ).build()

    @Provides
    @ApplicationScope
    fun provideAyasDao(treasureDatabase: TreasureDatabase): AyasDao = treasureDatabase.ayasDao()

    @Provides
    @ApplicationScope
    fun provideCountriesDao(treasureDatabase: TreasureDatabase): CountriesDao = treasureDatabase.countriesDao()

    @Provides
    @ApplicationScope
    fun provideNamesDao(treasureDatabase: TreasureDatabase): NamesDao = treasureDatabase.namesDao()

    @Provides
    @ApplicationScope
    fun provideSurahsDao(treasureDatabase: TreasureDatabase): SurahsDao = treasureDatabase.surahsDao()

    @Provides
    @ApplicationScope
    fun providePrayerTimesDao(treasureDatabase: TreasureDatabase): PrayerTimesDao = treasureDatabase.prayerTimesDao()
}