package com.stavro_xhardha.pockettreasure.ui.home

import com.stavro_xhardha.pockettreasure.brain.MSharedPreferences
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class HomeFragmentModule {

    @Provides
    @FragmentScope
    fun provideHomeViewModelFactory(provider: Provider<HomeViewModel>): HomeViewModelFactory =
        HomeViewModelFactory(provider)

    @Provides
    @FragmentScope
    fun provideHomeViewModel(homeRepository: HomeRepository, coroutineDispatcher: CoroutineDispatcher): HomeViewModel =
        HomeViewModel(homeRepository, coroutineDispatcher)

    @Provides
    @FragmentScope
    fun provideHomeRepository(treasureApi: TreasureApi, mSharedPreferences: MSharedPreferences): HomeRepository =
        HomeRepository(treasureApi, mSharedPreferences)
}