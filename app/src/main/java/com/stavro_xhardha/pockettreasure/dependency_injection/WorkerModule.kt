package com.stavro_xhardha.pockettreasure.dependency_injection

import com.stavro_xhardha.pockettreasure.PrayerWorkerFactory
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.rocket.Rocket
import dagger.Module
import dagger.Provides

@Module(includes = [PreferencesModule::class, NetworkModule::class])
class WorkerModule {

    @Provides
    @ApplicationScope
    fun providePrayerWorkManager(treasureApi: TreasureApi, rocket: Rocket): PrayerWorkerFactory =
        PrayerWorkerFactory(treasureApi, rocket)
}
