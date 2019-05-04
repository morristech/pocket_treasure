package com.stavro_xhardha.pockettreasure.ui.names

import com.stavro_xhardha.pockettreasure.model.NameResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class NamesRepository @Inject constructor(val treasureApi: TreasureApi) {


    fun fetchNintyNineNamesAsync(): Deferred<NameResponse> {
        return treasureApi.getNintyNineNamesAsync()
    }


}