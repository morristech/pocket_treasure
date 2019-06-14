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
        pocketTreasureComponent = DaggerPocketTreasureComponent.factory().create(this)
        INSTANCE = pocketTreasureComponent
    }

//    private fun initWorkManagerConfiguration() {
//        val prayerWorkerFactory = pocketTreasureComponent.prayerWorkerFactory()
//
//        val config = Configuration.Builder()
//            .setWorkerFactory(prayerWorkerFactory)
//            .build()
//
//        WorkManager.initialize(this, config)
//    }

    companion object {
        private var INSTANCE: PocketTreasureComponent? = null

        @JvmStatic
        fun getPocketTreasureComponent(): PocketTreasureComponent = INSTANCE!!
    }
}