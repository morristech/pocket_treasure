package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.CountriesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class SetupFragmentModule {

    @Provides
    @FragmentScope
    fun provideSetupViewModelFactory(provider: Provider<SetupViewModel>): SetupViewModelFactory =
        SetupViewModelFactory(provider)

    @Provides
    @FragmentScope
    fun provideSetupViewModel(setupRepository: SetupRepository): SetupViewModel = SetupViewModel(setupRepository)

    @Provides
    @FragmentScope
    fun provideSetupRepository(
        treasureApi: TreasureApi,
        mSharedPreferences: Rocket,
        treasureDatabase: TreasureDatabase
    ): SetupRepository =
        SetupRepository(treasureApi, mSharedPreferences, treasureDatabase.countriesDao())
}