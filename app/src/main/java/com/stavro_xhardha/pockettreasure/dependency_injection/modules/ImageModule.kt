package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import android.app.Application
import android.app.WallpaperManager
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ImageModule {

    @Provides
    @ApplicationScope
    fun providePicasso(): Picasso = Picasso.get()

    @Provides
    fun provideWallpaperManager(application: Application): WallpaperManager = WallpaperManager.getInstance(application)
}