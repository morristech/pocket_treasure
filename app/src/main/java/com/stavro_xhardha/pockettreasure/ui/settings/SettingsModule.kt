package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    @FragmentScope
    fun provideSettingsFragmentFactory(repository: SettingsRepository): SettingsFragmentFactory =
        SettingsFragmentFactory(repository)
}