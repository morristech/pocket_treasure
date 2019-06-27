package com.stavro_xhardha.pockettreasure.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.room_db.CountriesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CountryReadWrite {

    private lateinit var countiresDao: CountriesDao
    private lateinit var treasureDatabase: TreasureDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        treasureDatabase = Room.inMemoryDatabaseBuilder(context, TreasureDatabase::class.java).build()
        countiresDao = treasureDatabase.countriesDao()
    }

    @After
    fun tearDown() {
        treasureDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun testCountryWriteAndRead() = runBlocking {
        val countryToInsert = Country("SWEET", "HOME")
        countiresDao.insertCountry(countryToInsert)

        val insertedCountries = countiresDao.selectAllCountries()

        assertEquals(insertedCountries, listOf(countryToInsert))
    }
}