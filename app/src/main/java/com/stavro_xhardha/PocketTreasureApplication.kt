package com.stavro_xhardha

import android.app.Application
import com.stavro_xhardha.pockettreasure.dependency_injection.DaggerPocketTreasureComponent
import com.stavro_xhardha.pockettreasure.dependency_injection.PocketTreasureComponent
import net.danlew.android.joda.JodaTimeAndroid

class PocketTreasureApplication : Application() {
    private lateinit var pocketTreasureComponent: PocketTreasureComponent

    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
        pocketTreasureComponent = DaggerPocketTreasureComponent.builder().appContextModule(this)
            .build()
    }

    fun getPocketTreasureComponent() = pocketTreasureComponent
}