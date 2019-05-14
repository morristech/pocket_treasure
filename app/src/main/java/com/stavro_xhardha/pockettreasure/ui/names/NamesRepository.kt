package com.stavro_xhardha.pockettreasure.ui.names

import com.stavro_xhardha.pockettreasure.model.NameResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.Deferred
import retrofit2.Response
import javax.inject.Inject

class NamesRepository @Inject constructor(val treasureApi: TreasureApi) {

    fun fetchNintyNineNamesAsync(): Deferred<Response<NameResponse>> = treasureApi.getNintyNineNamesAsync()
}