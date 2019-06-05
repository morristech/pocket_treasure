package com.stavro_xhardha.pockettreasure.network

import com.stavro_xhardha.pockettreasure.model.Country
import com.stavro_xhardha.pockettreasure.model.NameResponse
import com.stavro_xhardha.pockettreasure.model.NewsResponse
import com.stavro_xhardha.pockettreasure.model.PrayerTimeResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface TreasureApi {

    @GET("asmaAlHusna")
    fun getNintyNineNamesAsync(): Deferred<Response<NameResponse>>

    @GET
    fun getCountriesListAsync(@Url url: String): Deferred<Response<ArrayList<Country>>>

    @GET("timingsByCity")
    fun getPrayerTimesTodayAsync(@Query("city") city: String?, @Query("country") country: String?, @Query("adjustment") adjustment: Int):
            Deferred<Response<PrayerTimeResponse>>

    @GET
    fun getLatestNewsAsync(
        @Url baseUrl: String,
        @Query("q") searchKeyWord: String,
        @Query("apiKey") apiKey: String,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Deferred<Response<NewsResponse>>
}