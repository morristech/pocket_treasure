package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import com.stavro_xhardha.pockettreasure.MainActivity
import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentModule::class])
    abstract fun contributeMainactivity(): MainActivity
}