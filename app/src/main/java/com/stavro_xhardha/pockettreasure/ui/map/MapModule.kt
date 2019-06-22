package com.stavro_xhardha.pockettreasure.ui.map

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module
class MapModule {

    @FragmentScope
    @Provides
    fun provideMapViewModelProvider(mapRepository: MapRepository): MapViewModelProviderFactory =
        MapViewModelProviderFactory(mapRepository)

    @FragmentScope
    @Provides
    fun provideMapRepository(treasureApi: TreasureApi, rocket: Rocket) = MapRepository(treasureApi, rocket)
}