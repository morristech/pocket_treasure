package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.FragmentScope
import com.stavro_xhardha.pockettreasure.ui.quran.QuranAdapterContract
import com.stavro_xhardha.pockettreasure.ui.quran.QuranFragment
import dagger.Binds
import dagger.Module

@Module
abstract class QuranModule {
    @Binds
    @FragmentScope
    abstract fun bindQuranAdapterContract(quranFragment: QuranFragment): QuranAdapterContract
}
