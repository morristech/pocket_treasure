package com.stavro_xhardha.pockettreasure.ui.names

import com.stavro_xhardha.pockettreasure.model.Name
import com.stavro_xhardha.pockettreasure.model.NameResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.NamesDao
import retrofit2.Response
import javax.inject.Inject

class NamesRepository @Inject constructor(private val treasureApi: TreasureApi, private val namesDao: NamesDao) {

    suspend fun fetchNintyNineNamesAsync(): Response<NameResponse> = treasureApi.getNintyNineNamesAsync()

    suspend fun countNamesInDatabase(): Int = namesDao.selectAllNames().size

    suspend fun getNamesFromDatabase(): List<Name> = namesDao.selectAllNames()

    suspend fun saveToDatabase(name: Name) {
        namesDao.insertName(name)
    }
}