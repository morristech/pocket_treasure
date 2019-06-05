package com.stavro_xhardha.pockettreasure.ui.news

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class NewsFragmentModule {

    @FragmentScope
    @Provides
    fun provideNewsDataSource(treasureApi: TreasureApi): NewsDataSource = NewsDataSource(treasureApi)

    @FragmentScope
    @Provides
    fun provideNewsViewModelFactory(
        newsDataSourceFactory: NewsDataSourceFactory
    ): NewsViewModelFactory =
        NewsViewModelFactory(newsDataSourceFactory)

    @FragmentScope
    @Provides
    fun provideNewsDataSourceFactory(newsDataSource: NewsDataSource): NewsDataSourceFactory =
        NewsDataSourceFactory(newsDataSource)
}