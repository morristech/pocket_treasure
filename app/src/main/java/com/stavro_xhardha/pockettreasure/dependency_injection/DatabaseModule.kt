package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import androidx.room.Room
import com.stavro_xhardha.pockettreasure.brain.TREASURE_DATABASE_NAME
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
    ).build()
}