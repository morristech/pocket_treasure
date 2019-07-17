package com.stavro_xhardha.pockettreasure.dependency_injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.pockettreasure.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    @ApplicationScope
    abstract fun bindViewModel(daggerViewModelFactory: DaggerVMFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ApplicationScope
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

}