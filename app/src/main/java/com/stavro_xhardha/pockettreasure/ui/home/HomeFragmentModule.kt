package com.stavro_xhardha.pockettreasure.ui.home

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class HomeFragmentModule {

    @Provides
    @FragmentScope
    fun provideHomeViewModelFactory(homeRepository: HomeRepository): HomeViewModelFactory =
        HomeViewModelFactory(homeRepository)

    @Provides
    @FragmentScope
    fun provideHomeRepository(treasureApi: TreasureApi, mSharedPreferences: Rocket): HomeRepository =
        HomeRepository(treasureApi, mSharedPreferences)
}