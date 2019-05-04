package com.stavro_xhardha.pockettreasure.model

import io.reactivex.Scheduler

data class RxJavaScheduler(
    val databaseScheduler: Scheduler,
    val diskScheduler: Scheduler,
    val networkScheduler: Scheduler,
    val mainThreadScheduler: Scheduler
)