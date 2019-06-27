package com.stavro_xhardha.pockettreasure.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.UnsplashResponse
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

class GalleryDataSource @Inject constructor(val treasureApi: TreasureApi) :
    PageKeyedDataSource<Int, UnsplashResult>() {

    private var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()
    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.invoke()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                networkState.postValue(NetworkState.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(params.key)
                if (primaryUnsplashResponse.code() == 200) {
                    retry = null
                    callback.onResult(primaryUnsplashResponse.body()!!.results, params.key.inc())
                    networkState.postValue(NetworkState.LOADED)
                } else {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error("Network Error"))
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                retry = {
                    loadAfter(params, callback)
                }
                networkState.postValue(NetworkState.error("Network Error"))
            }
        }
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UnsplashResult>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                initialLoad.postValue(NetworkState.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(1)
                if (primaryUnsplashResponse.code() == 200) {
                    initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(primaryUnsplashResponse.body()!!.results, null, 2)
                } else {
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error = NetworkState.error("Network error")
                    initialLoad.postValue(error)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                retry = {
                    loadInitial(params, callback)
                }
                val error = NetworkState.error("Network error")
                initialLoad.postValue(error)
            }
        }
    }

    private suspend fun getPhotosFromUsplashAPI(pageNumber: Int): Response<UnsplashResponse> =
        treasureApi.getUnsplashImagesAsync(
            UNSPLASH_BASE_URL, UNPLASH_QUERY_VALUE, pageNumber, INITIAL_PAGE_SIZE,
            CLIENT_ID, CLIENT_SECRET
        )
}