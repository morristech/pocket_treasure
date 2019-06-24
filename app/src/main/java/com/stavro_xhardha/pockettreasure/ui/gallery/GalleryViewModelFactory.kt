package com.stavro_xhardha.pockettreasure.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.util.concurrent.Executor
import javax.inject.Inject

class GalleryViewModelFactory @Inject constructor(
    private val galleryDataSourceFactory: GalleryDataSourceFactory,
    private val executor: Executor
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        GalleryViewModel(galleryDataSourceFactory, executor) as T
}