package com.stavro_xhardha.pockettreasure.ui.news

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
class NewsFragmentModule {

    @FragmentScope
    @Provides
    fun provideNewsDataSource(treasureApi: TreasureApi, executor: Executor): NewsDataSource =
        NewsDataSource(treasureApi, executor)

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

    @Provides
    fun provideExecutor(): Executor = Executors.newFixedThreadPool(5)
}