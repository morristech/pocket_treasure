package com.stavro_xhardha.pockettreasure.ui.home

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.ui.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [HomeFragmentModule::class], dependencies = [PocketTreasureComponent::class])
interface HomeComponent {
    fun inject(homeFragment: HomeFragment)
}