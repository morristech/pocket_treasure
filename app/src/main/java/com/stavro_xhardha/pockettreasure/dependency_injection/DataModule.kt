package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import com.stavro_xhardha.pockettreasure.brain.MSharedPreferences
import com.stavro_xhardha.pockettreasure.brain.SHARED_PREFERENCES_TAG
import dagger.Module
import dagger.Provides

@Module(includes = [AppContextModule::class])
class DataModule {

    @Provides
    @ApplicationScope
    fun provideRocket(context: Application): MSharedPreferences =
        MSharedPreferences.launch(context, SHARED_PREFERENCES_TAG)

}
