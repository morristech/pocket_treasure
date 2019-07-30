package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.pockettreasure.brain.MIGRATION_1_2
import com.stavro_xhardha.pockettreasure.brain.TREASURE_DATABASE_NAME
import com.stavro_xhardha.pockettreasure.dependency_injection.ApplicationScope
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import dagger.Module
import dagger.Provides

@Module
class DatabaseModule {
    @Provides
    @ApplicationScope
    fun provideRoomDatabase(context: Application): TreasureDatabase = Room.databaseBuilder(
        context,
        TreasureDatabase::class.java, TREASURE_DATABASE_NAME
    ).addMigrations(MIGRATION_1_2).build()

    @Provides
    @ApplicationScope
    fun providesNamesDao(database: TreasureDatabase) = database.namesDao()

    @Provides
    @ApplicationScope
    fun providesSurahsDao(database: TreasureDatabase) = database.surahsDao()

    @Provides
    @ApplicationScope
    fun providesAyahDao(database: TreasureDatabase) = database.ayasDao()

    @Provides
    @ApplicationScope
    fun providesPrayerTimesDao(database: TreasureDatabase) = database.prayerTimesDao()
}