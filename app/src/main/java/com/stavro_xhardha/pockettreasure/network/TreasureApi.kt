package com.stavro_xhardha.pockettreasure.network

import com.stavro_xhardha.pockettreasure.model.*
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

    @GET
    suspend fun getUnsplashImagesAsync(
        @Url baseUrl: String,
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") resultPerPage: Int,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String
    ): Response<UnsplashResponse>

//    @GET
//    suspend fun getLocations(
//        @Url baseUrl: String,
//        @Query("input") input: String,
//        @Query("inputtype") inputType: String,
//        @Query("key") key: String
//    ): Response<>

    @GET
    suspend fun getQuranDataAsync(@Url baseUrl: String): Response<QuranResponse>
}