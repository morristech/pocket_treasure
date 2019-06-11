package com.stavro_xhardha.pockettreasure.ui.gallery

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Component

@FragmentScope
@Component(modules = [GalleryModule::class], dependencies = [PocketTreasureComponent::class])
interface GalleryComponent {
    fun inject(galleryFragment: GalleryFragment)
}