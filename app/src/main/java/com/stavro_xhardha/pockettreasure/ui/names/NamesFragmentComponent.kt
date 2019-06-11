package com.stavro_xhardha.pockettreasure.ui.names

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [NamesFragmentModule::class], dependencies = [PocketTreasureComponent::class])
interface NamesFragmentComponent {
    fun inject(namesFragment: NamesFragment)
}