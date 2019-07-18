package com.stavro_xhardha.pockettreasure.dependency_injection

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters

interface ChildWorkerFactory {
    fun create(context: Context, parameters: WorkerParameters): ListenableWorker
}