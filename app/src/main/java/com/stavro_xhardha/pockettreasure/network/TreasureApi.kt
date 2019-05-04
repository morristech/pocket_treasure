package com.stavro_xhardha.pockettreasure.network

import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.model.Namesresponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Url

interface TreasureApi {

    @GET("asmaAlHusna")
    fun getNintyNineNamesAsync(): Deferred<Namesresponse>

    @GET
    fun getCountriesList(@Url url: String): Deferred<ArrayList<Country>>
}