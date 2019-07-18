package com.stavro_xhardha.pockettreasure.dependency_injection.modules

import com.squareup.inject.assisted.dagger2.AssistedModule
import com.stavro_xhardha.pockettreasure.background.PrayerSyncWorker
import com.stavro_xhardha.pockettreasure.dependency_injection.ChildWorkerFactory
import com.stavro_xhardha.pockettreasure.dependency_injection.WorkerKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@AssistedModule
@Module(includes = [AssistedInject_WorkerModule::class])
abstract class WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(PrayerSyncWorker::class)
    abstract fun bindHelloWorldWorker(factory: PrayerSyncWorker.PrayerFactory): ChildWorkerFactory
}