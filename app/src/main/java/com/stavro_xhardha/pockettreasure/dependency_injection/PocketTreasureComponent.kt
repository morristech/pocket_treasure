package com.stavro_xhardha.pockettreasure.dependency_injection

import android.app.Application
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.dependency_injection.modules.*
import com.stavro_xhardha.pockettreasure.dependency_injection.scopes.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule

@ApplicationScope
@Component(
    modules = [AndroidInjectionModule::class, ActivityModule::class, BroadCastReceiversModule::class, WorkerModule::class, NetworkModule::class,
        PreferencesModule::class, DatabaseModule::class, ImageModule::class, OfflineSchedulerModule::class, ViewModelFactoryModule::class]
)
interface PocketTreasureComponent {
    fun inject(pocketTreasureApplication: PocketTreasureApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): PocketTreasureComponent
    }
}