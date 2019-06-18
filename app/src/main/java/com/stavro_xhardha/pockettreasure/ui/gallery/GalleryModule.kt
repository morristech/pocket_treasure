package com.stavro_xhardha.pockettreasure.ui.gallery

import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Module
import dagger.Provides

@Module
class GalleryModule(val fragment: GalleryFragment) {

    @Provides
    @FragmentScope
    fun provideGalleryFragment(): GalleryFragment = fragment

    @Provides
    @FragmentScope
    fun provideGalleryDataSource(treasureApi: TreasureApi): GalleryDataSource = GalleryDataSource(treasureApi)

    @Provides
    @FragmentScope
    fun provideGalleryDataSourceFactory(galleryDataSource: GalleryDataSource): GalleryDataSourceFactory =
        GalleryDataSourceFactory(galleryDataSource)

    @Provides
    @FragmentScope
    fun provideGalleryFragmentFactory(galleryDataSourceFactory: GalleryDataSourceFactory): GalleryViewModelFactory =
        GalleryViewModelFactory(galleryDataSourceFactory)

    @Provides
    @FragmentScope
    fun provideGalleryAdapter(galleryFragment: GalleryFragment, picasso: Picasso): GalleryAdapter =
        GalleryAdapter(galleryFragment, picasso)
}