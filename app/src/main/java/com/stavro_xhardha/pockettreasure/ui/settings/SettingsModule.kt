package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    @FragmentScope
    fun provideSettingsFragmentFactory(repository: SettingsRepository): SettingsFragmentFactory =
        SettingsFragmentFactory(repository)
}