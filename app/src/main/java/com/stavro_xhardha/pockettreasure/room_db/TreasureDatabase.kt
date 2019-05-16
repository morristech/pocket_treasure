package com.stavro_xhardha.pockettreasure.room_db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.stavro_xhardha.pockettreasure.model.Name

@Database(entities = [Name::class], version = 1, exportSchema = false)
abstract class TreasureDatabase : RoomDatabase() {
    abstract fun namesDao(): NamesDao
}