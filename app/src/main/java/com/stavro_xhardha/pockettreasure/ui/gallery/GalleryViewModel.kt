package com.stavro_xhardha.pockettreasure.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.brain.INITIAL_PAGE_SIZE
import com.stavro_xhardha.pockettreasure.brain.buildPagedList
import com.stavro_xhardha.pockettreasure.model.UnsplashResponse
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import com.stavro_xhardha.pockettreasure.ui.news.InitialState
import com.stavro_xhardha.pockettreasure.ui.news.NetworkStatus
import java.util.concurrent.Executors

class GalleryViewModel(galleryDataSourceFactory: GalleryDataSourceFactory) : ViewModel() {
    private var unsplashLiveData: LiveData<PagedList<UnsplashResult>>
    var networkStatus: LiveData<NetworkStatus>
    var primaryNetworkStatus: LiveData<InitialState>

    init {
        val executor = Executors.newFixedThreadPool(5)
        val config = buildPagedList()

        networkStatus = Transformations.switchMap(galleryDataSourceFactory.mutableDataSource) {
            it.getNetworkStatus()
        }

        primaryNetworkStatus = Transformations.switchMap(galleryDataSourceFactory.mutableDataSource) {
            it.getInitialState()
        }

        unsplashLiveData = LivePagedListBuilder<Int, UnsplashResult>(galleryDataSourceFactory, config)
            .setFetchExecutor(executor).build()
    }

    fun getUnsplashLiveData(): LiveData<PagedList<UnsplashResult>> = unsplashLiveData
}