package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.brain.MSharedPreferences
import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Module
import dagger.Provides
import javax.inject.Provider

@Module
class SetupFragmentModule {

    @Provides
    @FragmentScope
    fun provideSetupViewModelFactory(provider: Provider<SetupViewModel>): SetupViewModelFactory =
        SetupViewModelFactory(provider = provider)

    @Provides
    @FragmentScope
    fun provideSetupViewModel(
        setupRepository: SetupRepository,
        coroutinesDispatcher: CoroutineDispatcher
    ): SetupViewModel = SetupViewModel(setupRepository, coroutinesDispatcher)

    @Provides
    @FragmentScope
    fun provideSetupRepository(treasureApi: TreasureApi, mSharedPreferences: MSharedPreferences): SetupRepository =
        SetupRepository(treasureApi, mSharedPreferences)
}