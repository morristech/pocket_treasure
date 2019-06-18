package com.stavro_xhardha.pockettreasure.ui.gallery

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.UnsplashResponse
import com.stavro_xhardha.pockettreasure.model.UnsplashResult
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.brain.InitialNetworkState
import com.stavro_xhardha.pockettreasure.brain.CurrentNetworkStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class GalleryDataSource @Inject constructor(val treasureApi: TreasureApi) : PageKeyedDataSource<Int, UnsplashResult>() {
    private val networkCurrentNetworkStatus: MutableLiveData<CurrentNetworkStatus> = MutableLiveData()
    private val initialNetworkState: MutableLiveData<InitialNetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, UnsplashResult>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(CurrentNetworkStatus.LOADING)
                updateInitialState(InitialNetworkState.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(1)
                if (primaryUnsplashResponse.code() == 200) {
                    callback.onResult(primaryUnsplashResponse.body()!!.results, null, 2)
                    updateNetworkStatus(CurrentNetworkStatus.SUCCESS)
                    updateInitialState(InitialNetworkState.SUCCESS)
                } else {
                    updateInitialState(InitialNetworkState.ERROR)
                }
            } catch (exception: Exception) {
                updateInitialState(InitialNetworkState.ERROR)
                exception.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UnsplashResult>) {
        if (isDebugMode)
            Log.d(APPLICATION_TAG, params.key.toString())
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(CurrentNetworkStatus.LOADING)
                val primaryUnsplashResponse = getPhotosFromUsplashAPI(params.key)
                if (primaryUnsplashResponse.code() == 200) {
                    callback.onResult(primaryUnsplashResponse.body()!!.results, params.key.inc())
                    updateNetworkStatus(CurrentNetworkStatus.SUCCESS)
                } else {
                    updateNetworkStatus(CurrentNetworkStatus.FAILED)
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                updateNetworkStatus(CurrentNetworkStatus.FAILED)
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

    private suspend fun updateNetworkStatus(networkCurrentNetworkStatus: CurrentNetworkStatus) {
        withContext(Dispatchers.Main) {
            this@GalleryDataSource.networkCurrentNetworkStatus.value = networkCurrentNetworkStatus
        }
    }

    private suspend fun updateInitialState(networkStatus: InitialNetworkState) {
        withContext(Dispatchers.Main) {
            initialNetworkState.value = networkStatus
        }
    }

    fun getNetworkStatus(): MutableLiveData<CurrentNetworkStatus> = networkCurrentNetworkStatus

    fun getInitialState(): MutableLiveData<InitialNetworkState> = initialNetworkState
}