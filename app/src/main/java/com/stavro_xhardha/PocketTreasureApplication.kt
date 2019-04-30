package com.stavro_xhardha

import android.app.Application
import com.stavro_xhardha.pockettreasure.dependency_injection.DaggerPocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent

class PocketTreasureApplication : Application() {
    private lateinit var pocketTreasureComponent: PocketTreasureComponent

    override fun onCreate() {
        super.onCreate()
        pocketTreasureComponent = DaggerPocketTreasureComponent.builder().build()
    }

    fun getPocketTreasureComponent() = pocketTreasureComponent
}