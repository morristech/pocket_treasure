package com.stavro_xhardha.pockettreasure.dependency_injection

import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import dagger.Component

@ApplicationScope
@Component(modules = [NetworkModule::class, SchedulersModule::class])
interface PocketTreasureComponent {
    fun getTreasureApi(): TreasureApi

    fun getSchedulers(): CoroutineDispatcher
}