package com.stavro_xhardha.pockettreasure.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class PrayerTimeWorkManager(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    override suspend fun doWork(): Result = coroutineScope {
        launch {

        }
        Result.success()
    }
}