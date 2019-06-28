package com.stavro_xhardha.pockettreasure.ui.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.News
import com.stavro_xhardha.rocket.Rocket

class NewsViewModel(
    dataSourceFactory: NewsDataSourceFactory,
    val rocket: Rocket
) : ViewModel() {
    private var listing: Listing<News>
    private val _enterDialogVisibility = MutableLiveData<Boolean>()
    var enterDialogVisibility = _enterDialogVisibility
    var newsData: LiveData<PagedList<News>>
    var networkState: LiveData<NetworkState>
    var refreshState: LiveData<NetworkState>
    val hasEnteredNewsYet = rocket.readBoolean(HAS_ONCE_ENTERED_NEWS)

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

        checkDialogVisibility()
    }

    private fun checkDialogVisibility() {
        if (!hasEnteredNewsYet) {
            _enterDialogVisibility.value = true
            rocket.writeBoolean(HAS_ONCE_ENTERED_NEWS, true)
        }
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