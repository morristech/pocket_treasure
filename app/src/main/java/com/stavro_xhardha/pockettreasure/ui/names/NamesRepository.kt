package com.stavro_xhardha.pockettreasure.ui.names

import com.stavro_xhardha.pockettreasure.model.Namesresponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.Deferred
import javax.inject.Inject

class NamesRepository @Inject constructor(val treasureApi: TreasureApi) {


    fun fetchNintyNineNamesAsync(): Deferred<Namesresponse> {
        return treasureApi.getNintyNineNamesAsync()
    }


}