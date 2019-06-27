package com.stavro_xhardha.pockettreasure.ui.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.News

class NewsViewModel(
    dataSourceFactory: NewsDataSourceFactory
) : ViewModel() {
    private var listing: Listing<News>
    var newsData: LiveData<PagedList<News>>
    var networkState: LiveData<NetworkState>
    var refreshState: LiveData<NetworkState>

    init {
        val config = buildPagedList()

        val livePagedListBuilder =
            LivePagedListBuilder(dataSourceFactory, config).build()

        val refreshStateListener = Transformations.switchMap(dataSourceFactory.mutableDataSource) {
            it.initialLoad
        }

        listing = Listing(
            pagedList = livePagedListBuilder,
            networkState = Transformations.switchMap(dataSourceFactory.mutableDataSource) {
                it.networkState
            },
            retry = {
                dataSourceFactory.mutableDataSource.value?.retryAllFailed()
            },
            refresh = {
                dataSourceFactory.mutableDataSource.value?.invalidate()
            },
            refreshState = refreshStateListener
        )

        newsData = listing.pagedList
        networkState = listing.networkState
        refreshState = listing.refreshState
    }

    fun retry() {
        if (isDebugMode)
            Log.d(APPLICATION_TAG, "RETRYING")
        listing.retry.invoke()
    }

    fun newsDataList(): LiveData<PagedList<News>> = newsData

    fun currentNetworkState(): LiveData<NetworkState> = networkState

    fun initialNetworkState(): LiveData<NetworkState> = refreshState
}