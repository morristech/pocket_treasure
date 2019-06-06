package com.stavro_xhardha.pockettreasure.ui.gallery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.UnsplashResponse
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.ui.news.InitialState
import com.stavro_xhardha.pockettreasure.ui.news.NetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class GalleryDataSource @Inject constructor(val treasureApi: TreasureApi) : PageKeyedDataSource<Int, UnsplashResult>() {
    private val networkNetworkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()
    private val initialState: MutableLiveData<InitialState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UnsplashResult>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(NetworkStatus.LOADING)
                updateInitialState(InitialState.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(1)
                if (primaryUnsplashResponse.code() == 200) {
                    callback.onResult(primaryUnsplashResponse.body()!!.results, null, 2)
                    updateNetworkStatus(NetworkStatus.SUCCESS)
                    updateInitialState(InitialState.SUCCESS)
                } else {
                    updateInitialState(InitialState.ERROR)
                }
            } catch (exception: Exception) {
                updateInitialState(InitialState.ERROR)
                exception.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
        if (isDebugMode)
            Log.d(APPLICATION_TAG, params.key.toString())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(NetworkStatus.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(params.key)
                if (primaryUnsplashResponse.code() == 200) {
                    callback.onResult(primaryUnsplashResponse.body()!!.results, params.key.inc())
                    updateNetworkStatus(NetworkStatus.SUCCESS)
                } else {
                    updateNetworkStatus(NetworkStatus.FAILED)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                updateNetworkStatus(NetworkStatus.FAILED)
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
    }

    private suspend fun getPhotosFromUsplashAPI(pageNumber: Int): Response<UnsplashResponse> =
        treasureApi.getUnsplashImagesAsync(
            UNSPLASH_BASE_URL, UNPLASH_QUERY_VALUE, pageNumber, INITIAL_PAGE_SIZE,
            CLIENT_ID, CLIENT_SECRET
        )

    private suspend fun updateNetworkStatus(networkNetworkStatus: NetworkStatus) {
        withContext(Dispatchers.Main) {
            this@GalleryDataSource.networkNetworkStatus.value = networkNetworkStatus
        }
    }

    private suspend fun updateInitialState(networkStatus: InitialState) {
        withContext(Dispatchers.Main) {
            initialState.value = networkStatus
        }
    }

    fun getNetworkStatus(): MutableLiveData<NetworkStatus> = networkNetworkStatus

    fun getInitialState(): MutableLiveData<InitialState> = initialState
}