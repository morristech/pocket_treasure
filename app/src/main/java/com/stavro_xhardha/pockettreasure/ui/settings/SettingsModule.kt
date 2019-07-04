package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module
class SettingsModule {

    @Provides
    @FragmentScope
    fun provideSettingsFragmentFactory(repository: SettingsRepository): SettingsFragmentFactory =
        SettingsFragmentFactory(repository)

    @Provides
    @FragmentScope
    fun provideSettingsRepository(rocket: Rocket, treasureDatabase: TreasureDatabase): SettingsRepository =
        SettingsRepository(rocket, treasureDatabase.prayerTimesDao())
}