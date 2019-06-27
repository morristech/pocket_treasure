package com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module
class CountrySettingsModule {

    @Provides
    @FragmentScope
    fun provideCountrySettingsViewModelFactory(countrySelectionRepository: CountrySelectionRepository): CountrySettingsViewModelFactory =
        CountrySettingsViewModelFactory(countrySelectionRepository)

    @Provides
    @FragmentScope
    fun provideCountrySelectionRepository(
        treasureDatabase: TreasureDatabase,
        rocket: Rocket
    ): CountrySelectionRepository =
        CountrySelectionRepository(treasureDatabase.countriesDao(), rocket)
}