package com.stavro_xhardha.pockettreasure.ui.quran.aya

import com.stavro_xhardha.pockettreasure.dependency_injection.QuranSubScope
import com.stavro_xhardha.pockettreasure.ui.quran.QuranComponent
import dagger.Component

@QuranSubScope
@Component(modules = [AyaModule::class], dependencies = [QuranComponent::class])
interface AyaComponent {
    fun inject(ayaFragment: AyaFragment)
}