package com.stavro_xhardha.pockettreasure.model

import kotlinx.coroutines.CoroutineDispatcher

data class CoroutineDispatcher(
    val databaseSchedulers: CoroutineDispatcher,
    val disk: CoroutineDispatcher,
    val network: CoroutineDispatcher,
    val mainThread: CoroutineDispatcher
)