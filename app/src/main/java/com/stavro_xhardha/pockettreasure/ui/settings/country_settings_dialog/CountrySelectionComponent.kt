package com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import dagger.Component

@Component(modules = [CountrySettingsModule::class], dependencies = [PocketTreasureComponent::class])
@FragmentScope
interface CountrySelectionComponent {
    fun inject(countrySettingsFragment: CountryAndCapitalSelectionFragment)
}