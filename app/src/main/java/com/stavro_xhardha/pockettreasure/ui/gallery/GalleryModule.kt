package com.stavro_xhardha.pockettreasure.ui.gallery

import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors

@Module
class GalleryModule(val fragment: GalleryFragment) {

    @Provides
    @FragmentScope
    fun provideGalleryFragment(): GalleryFragment = fragment

    @Provides
    @FragmentScope
    fun provideGalleryDataSource(treasureApi: TreasureApi, executor: Executor): GalleryDataSource =
        GalleryDataSource(treasureApi, executor)

    @Provides
    @FragmentScope
    fun provideGalleryDataSourceFactory(galleryDataSource: GalleryDataSource): GalleryDataSourceFactory =
        GalleryDataSourceFactory(galleryDataSource)

    @Provides
    @FragmentScope
    fun provideGalleryFragmentFactory(
        galleryDataSourceFactory: GalleryDataSourceFactory,
        executor: Executor
    ): GalleryViewModelFactory =
        GalleryViewModelFactory(galleryDataSourceFactory, executor)

    @Provides
    @FragmentScope
    fun provideGalleryAdapter(galleryFragment: GalleryFragment, picasso: Picasso): GalleryAdapter =
        GalleryAdapter(galleryFragment, picasso)

    @Provides
    fun provideExecutor(): Executor = Executors.newFixedThreadPool(5)
}