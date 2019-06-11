package com.stavro_xhardha.pockettreasure.ui.setup

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [SetupFragmentModule::class], dependencies = [PocketTreasureComponent::class])
interface SetupComponent {
    fun inject(setupFragment: SetupFragment)
}