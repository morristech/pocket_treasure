package com.stavro_xhardha.pockettreasure.dependency_injection

import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, PreferencesModule::class, AppContextModule::class])
interface PocketTreasureComponent {
    fun getTreasureApi(): TreasureApi

    fun getSharedPreferences(): Rocket
}