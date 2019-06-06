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
    suspend fun getNintyNineNamesAsync(): Response<NameResponse>

    @GET
    suspend fun getCountriesListAsync(@Url url: String): Response<ArrayList<Country>>

    @GET("timingsByCity")
    suspend fun getPrayerTimesTodayAsync(@Query("city") city: String?, @Query("country") country: String?, @Query("adjustment") adjustment: Int):
            Response<PrayerTimeResponse>

    @GET
    suspend fun getLatestNewsAsync(
        @Url baseUrl: String,
        @Query("q") searchKeyWord: String,
        @Query("apiKey") apiKey: String,
        @Query("page") pageNumber: Int,
        @Query("pageSize") pageSize: Int
    ): Response<NewsResponse>
}