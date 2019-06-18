package com.stavro_xhardha.pockettreasure.dependency_injection

import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class PicassoModule {

    @Provides
    @ApplicationScope
    fun providePicasso(): Picasso = Picasso.get()
}