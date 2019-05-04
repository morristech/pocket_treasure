package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class AppContextModule(private val context: Application) {

    @Provides
    @ApplicationScope
    fun provideApplicationContext(): Application = context
}