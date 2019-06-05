package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.model.News
import java.util.concurrent.Executors

class NewsViewModel(
    dataSourceFactory: NewsDataSourceFactory
) : ViewModel() {
    private var newsLiveData: LiveData<PagedList<News>>
    var networkNetworkStatus: LiveData<NetworkStatus>
    var primaryNetworkStatus: LiveData<InitialState>

    init {
        val executor = Executors.newFixedThreadPool(5)
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        networkNetworkStatus =
            Transformations.switchMap(dataSourceFactory.mutableDataSource) {
                it.getNetworkStatus()
            }
        primaryNetworkStatus = Transformations.switchMap(dataSourceFactory.mutableDataSource) {
            it.getInitialState()
        }
        newsLiveData = LivePagedListBuilder<Int, News>(dataSourceFactory, config).setFetchExecutor(executor).build()
    }

    fun getNewsPost(): LiveData<PagedList<News>> = newsLiveData
}