package com.stavro_xhardha.pockettreasure.dependency_injection

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.stavro_xhardha.pockettreasure.brain.PRAYER_API_BASE_URL
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

    @Provides
    @ApplicationScope
    fun provideTreasureApi(retrofit: Retrofit): TreasureApi = retrofit.create(TreasureApi::class.java)

    @Provides
    @ApplicationScope
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(PRAYER_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
}