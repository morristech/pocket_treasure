package com.stavro_xhardha.pockettreasure.dependency_injection

import com.stavro_xhardha.pockettreasure.ui.gallery.GalleryFragment
import com.stavro_xhardha.pockettreasure.ui.gallery.full_image.FullImageFragment
import com.stavro_xhardha.pockettreasure.ui.home.HomeFragment
import com.stavro_xhardha.pockettreasure.ui.names.NamesFragment
import com.stavro_xhardha.pockettreasure.ui.news.NewsFragment
import com.stavro_xhardha.pockettreasure.ui.quran.QuranFragment
import com.stavro_xhardha.pockettreasure.ui.quran.aya.AyaFragment
import com.stavro_xhardha.pockettreasure.ui.settings.SettingsFragment
import com.stavro_xhardha.pockettreasure.ui.settings.country_settings_dialog.CountryAndCapitalSelectionFragment
import com.stavro_xhardha.pockettreasure.ui.setup.SetupFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributesGalleryFragment(): GalleryFragment

    @ContributesAndroidInjector
    abstract fun contributesNamesFragment(): NamesFragment

    @ContributesAndroidInjector
    abstract fun conributesNewsFragment(): NewsFragment

    @ContributesAndroidInjector
    abstract fun contributesQuranFragment(): QuranFragment

    @ContributesAndroidInjector
    abstract fun contributesSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributesSetupFragment(): SetupFragment

    @ContributesAndroidInjector
    abstract fun contributeFullImageFragment(): FullImageFragment

    @ContributesAndroidInjector
    abstract fun contributeAyasFragment(): AyaFragment

    @ContributesAndroidInjector
    abstract fun contributeCountrySelectionFragment(): CountryAndCapitalSelectionFragment
}