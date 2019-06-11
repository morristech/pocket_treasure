package com.stavro_xhardha.pockettreasure.ui.quran

import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.FragmentScope
import com.stavro_xhardha.pockettreasure.room_db.AyasDao
import dagger.Component

@FragmentScope
@Component(modules = [QuranModule::class], dependencies = [PocketTreasureComponent::class])
interface QuranComponent {
    fun getAyasDao(): AyasDao

    fun inject(QuranFragment: QuranFragment)
}