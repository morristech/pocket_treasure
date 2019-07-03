package com.stavro_xhardha.pockettreasure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.model.Surah
import com.stavro_xhardha.pockettreasure.room_db.SurahsDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SurahReadWriteTest {

    private lateinit var surahsDao: SurahsDao
    private lateinit var treasureDatabase: TreasureDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        treasureDatabase = Room.inMemoryDatabaseBuilder(context, TreasureDatabase::class.java).build()
        surahsDao = treasureDatabase.surahsDao()
    }

    @After
    fun finish() {
        treasureDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeSurahAndReadIt() = runBlocking {
        //Arrange
        val surah = Surah(1, "Abc", "Def", "Ghi", "Jkl", listOf())

        //Act
        surahsDao.insertSurah(surah)
        val insertedSurah = surahsDao.getAllSuras()

        //Assert
        assertEquals(listOf(surah), insertedSurah)
    }
}