package com.stavro_xhardha.pockettreasure.dependency_injection

import com.stavro_xhardha.pockettreasure.model.CoroutineDispatcher
import com.stavro_xhardha.pockettreasure.model.RxJavaScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.asCoroutineDispatcher

@Module
class SchedulersModule {

    @Provides
    @ApplicationScope
    fun provideApplicationSchedulers(): RxJavaScheduler =
        RxJavaScheduler(
            databaseScheduler = Schedulers.single(), diskScheduler = Schedulers.io(),
            networkScheduler = Schedulers.io(), mainThreadScheduler = AndroidSchedulers.mainThread()
        )

    @Provides
    @ApplicationScope
    fun provideCoroutinesDispatcher(rxJavaScheduler: RxJavaScheduler): CoroutineDispatcher =
        CoroutineDispatcher(
            databaseSchedulers = rxJavaScheduler.databaseScheduler.asCoroutineDispatcher(),
            disk = rxJavaScheduler.diskScheduler.asCoroutineDispatcher(),
            network = rxJavaScheduler.networkScheduler.asCoroutineDispatcher(),
            mainThread = rxJavaScheduler.mainThreadScheduler.asCoroutineDispatcher()
        )
}