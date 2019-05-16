package com.stavro_xhardha.pockettreasure.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stavro_xhardha.pockettreasure.model.Name

@Dao
interface NamesDao {

    @Query("SELECT * FROM names_of_creator")
    suspend fun selectAllNames(): List<Name>

    @Insert
    suspend fun insertName(name: Name)

    @Query("SELECT * FROM names_of_creator where id =:number")
    fun findName(number: Int): Name
}