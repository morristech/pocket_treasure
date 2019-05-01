package com.stavro_xhardha.pockettreasure.network

import com.stavro_xhardha.pockettreasure.model.Namesresponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface TreasureApi {

    @GET("asmaAlHusna")
    fun getNintyNineNamesAsync(): Deferred<Namesresponse>
}