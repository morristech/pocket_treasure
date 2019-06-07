package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [SettingsModule::class], dependencies = [PocketTreasureComponent::class])
interface SettingsFragmentComponent {
    fun inject(fragment: SettingsFragment)
}