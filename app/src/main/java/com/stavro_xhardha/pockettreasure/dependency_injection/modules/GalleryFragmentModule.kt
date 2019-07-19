package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.FragmentScope
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryContract
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryFragment
import dagger.Binds
import dagger.Module

@Module
abstract class GalleryFragmentModule {
    @Binds
    @FragmentScope
    abstract fun bindGalleryAdapterContract(galleryFragment: GalleryFragment): GalleryContract
}
