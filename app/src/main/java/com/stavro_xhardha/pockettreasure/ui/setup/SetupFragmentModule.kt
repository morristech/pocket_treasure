package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
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
    fun provideSetupRepository(treasureApi: TreasureApi, mSharedPreferences: Rocket): SetupRepository =
        SetupRepository(treasureApi, mSharedPreferences)
}