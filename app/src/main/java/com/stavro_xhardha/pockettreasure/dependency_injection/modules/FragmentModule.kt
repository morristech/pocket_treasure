package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.FragmentScope
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

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesGalleryFragment(): GalleryFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeFullImageFragment(): FullImageFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesNamesFragment(): NamesFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun conributesNewsFragment(): NewsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesQuranFragment(): QuranFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesSettingsFragment(): SettingsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributesSetupFragment(): SetupFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeAyasFragment(): AyaFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun contributeCountrySelectionFragment(): CountryAndCapitalSelectionFragment
}