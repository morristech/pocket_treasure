package com.stavro_xhardha.pockettreasure.dependency_injection.module

import android.app.Application
import android.app.WallpaperManager
import android.media.MediaPlayer
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.dependency_injection.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class MediaModule {

    @Provides
    @ApplicationScope
    fun providePicasso(): Picasso = Picasso.get()

    @Provides
    fun provideWallpaperManager(application: Application): WallpaperManager = WallpaperManager.getInstance(application)

    @Provides
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()
}