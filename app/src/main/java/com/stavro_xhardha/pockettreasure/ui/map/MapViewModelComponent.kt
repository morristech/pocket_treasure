package com.stavro_xhardha.pockettreasure.ui.map

import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import dagger.Component

@FragmentScope
@Component(modules = [MapModule::class], dependencies = [PocketTreasureComponent::class])
interface MapViewModelComponent {
    //fun inject(mapFragment: MapFragment)
}