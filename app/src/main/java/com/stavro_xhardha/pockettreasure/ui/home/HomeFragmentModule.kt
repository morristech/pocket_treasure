package com.stavro_xhardha.pockettreasure.ui.home

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import com.stavro_xhardha.rocket.Rocket
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
    fun provideHomeViewModel(homeRepository: HomeRepository): HomeViewModel =
        HomeViewModel(homeRepository)

    @Provides
    @FragmentScope
    fun provideHomeRepository(treasureApi: TreasureApi, mSharedPreferences: Rocket): HomeRepository =
        HomeRepository(treasureApi, mSharedPreferences)
}