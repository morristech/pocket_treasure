package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import android.app.WallpaperManager
import com.squareup.picasso.Picasso
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, PreferencesModule::class, DatabaseModule::class, ImageModule::class])
interface PocketTreasureComponent {
    fun getTreasureApi(): TreasureApi

    fun getSharedPreferences(): Rocket

    fun treasureDatabase(): TreasureDatabase

    fun picasso(): Picasso

    fun wallpaperManager(): WallpaperManager

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance pocketTreasureApplication: Application): PocketTreasureComponent
    }
}