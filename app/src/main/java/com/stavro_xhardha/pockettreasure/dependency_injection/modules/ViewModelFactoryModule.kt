package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stavro_xhardha.pockettreasure.dependency_injection.DaggerViewModelFactory
import com.stavro_xhardha.pockettreasure.dependency_injection.ViewModelKey
import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.ApplicationScope
import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.FragmentScope
import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryViewModel
import com.stavro_xhardha.pockettreasure.ui.home.HomeViewModel
import com.stavro_xhardha.pockettreasure.ui.names.NamesViewModel
import com.stavro_xhardha.pockettreasure.ui.news.NewsViewModel
import com.stavro_xhardha.pockettreasure.ui.quran.QuranViewModel
import com.stavro_xhardha.pockettreasure.ui.quran.aya.AyaViewModel
import com.stavro_xhardha.pockettreasure.ui.settings.SettingsViewModel
import com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog.CountrySettingsViewModel
import com.stavro_xhardha.pockettreasure.ui.setup.SetupViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModel(daggerViewModelFactory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SetupViewModel::class)
    abstract fun bindSetupViewModel(setupViewModel: SetupViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NamesViewModel::class)
    abstract fun bindNamesViewModel(namesViewModel: NamesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(QuranViewModel::class)
    abstract fun bindQuranViewModel(quranViewModel: QuranViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AyaViewModel::class)
    abstract fun bindAyasViewModel(ayasViewmodel: AyaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(GalleryViewModel::class)
    abstract fun bindGalleryViewModel(galleryViewModel: GalleryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CountrySettingsViewModel::class)
    abstract fun bindCountrySelectionViewModel(countrySeleViewModel: CountrySettingsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel::class)
    abstract fun bindNewsViewModel(newsViewModel: NewsViewModel): ViewModel
}