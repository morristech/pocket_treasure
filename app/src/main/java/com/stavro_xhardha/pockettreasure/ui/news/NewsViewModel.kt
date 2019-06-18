package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.brain.InitialNetworkState
import com.stavro_xhardha.pockettreasure.brain.CurrentNetworkStatus
import com.stavro_xhardha.pockettreasure.brain.buildPagedList
import com.stavro_xhardha.pockettreasure.model.News
import java.util.concurrent.Executors

class NewsViewModel(
    dataSourceFactory: NewsDataSourceFactory
) : ViewModel() {
    private var newsLiveData: LiveData<PagedList<News>>
    var networkCurrentNetworkStatus: LiveData<CurrentNetworkStatus>
    var primaryNetworkStatus: LiveData<InitialNetworkState>

    init {
        val executor = Executors.newFixedThreadPool(5)
        val config = buildPagedList()

        networkCurrentNetworkStatus =
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