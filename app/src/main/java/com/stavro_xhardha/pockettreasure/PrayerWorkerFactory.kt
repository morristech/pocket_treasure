package com.stavro_xhardha.pockettreasure

import android.app.Application
import android.content.Context
import androidx.work.*
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.worker.PrayerWorker
import com.stavro_xhardha.rocket.Rocket


class PrayerWorkerFactory(private val treasureApi: TreasureApi, private val rocket: Rocket) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val workerClass = Class.forName(workerClassName).asSubclass(CoroutineWorker::class.java)
        val constructor = workerClass.getDeclaredConstructor(
            Context::class.java,
            WorkerParameters::class.java,
            TreasureApi::class.java,
            Rocket::class.java
        )

        val instance = constructor.newInstance(appContext, workerParameters , treasureApi , rocket)

        when (instance) {
            is PrayerWorker -> {
                instance.rocket = rocket
                instance.treasureApi = treasureApi
            }
        }

        return instance
    }
}