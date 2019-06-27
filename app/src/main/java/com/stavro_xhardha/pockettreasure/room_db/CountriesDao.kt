package com.stavro_xhardha.pockettreasure.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stavro_xhardha.pockettreasure.model.Country

@Dao
interface CountriesDao {
    @Query("SELECT * FROM countries")
    suspend fun selectAllCountries(): List<Country>

    @Insert
    suspend fun insertCountry(country: Country)
}