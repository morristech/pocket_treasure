package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.pockettreasure.brain.INITIAL_PAGE_SIZE
import com.stavro_xhardha.pockettreasure.brain.NEWS_BASE_URL
import com.stavro_xhardha.pockettreasure.brain.SEARCH_KEY_WORD
import com.stavro_xhardha.pockettreasure.brain.SEARCH_NEWS_API_KEY
import com.stavro_xhardha.pockettreasure.model.News
import com.stavro_xhardha.pockettreasure.model.NewsResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.*
import retrofit2.Response
import javax.inject.Inject

class NewsDataSource @Inject constructor(val treasureApi: TreasureApi) : PageKeyedDataSource<Int, News>() {
    private val networkNetworkStatus: MutableLiveData<NetworkStatus> = MutableLiveData()
    private val initialState: MutableLiveData<InitialState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(NetworkStatus.LOADING)
                updateInitialState(InitialState.LOADING)
                val primaryNewsResponse = callLatestNewsAsync(1).await()
                if (primaryNewsResponse.isSuccessful) {
                    callback.onResult(primaryNewsResponse.body()!!.articles, null, 2)
                    updateNetworkStatus(NetworkStatus.SUCCESS)
                    updateInitialState(InitialState.SUCCESS)
                } else {
                    updateInitialState(InitialState.ERROR)
                }
            } catch (e: Exception) {
                updateInitialState(InitialState.ERROR)
                e.printStackTrace()
            }
        }
    }

    private suspend fun updateInitialState(networkStatus: InitialState) {
        withContext(Dispatchers.Main) {
            initialState.value = networkStatus
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(NetworkStatus.LOADING)
                val primaryNewsResponse = callLatestNewsAsync(params.key).await()
                if (primaryNewsResponse.isSuccessful) {
                    callback.onResult(primaryNewsResponse.body()!!.articles, params.key.inc())
                    updateNetworkStatus(NetworkStatus.SUCCESS)
                } else {
                    updateNetworkStatus(NetworkStatus.FAILED)
                }
            } catch (e: Exception) {
                updateNetworkStatus(NetworkStatus.FAILED)
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(NetworkStatus.LOADING)
                val primaryNewsResponse = callLatestNewsAsync(params.key).await()
                if (primaryNewsResponse.isSuccessful) {
                    callback.onResult(primaryNewsResponse.body()!!.articles, params.key.dec())
                    updateNetworkStatus(NetworkStatus.SUCCESS)
                } else {
                    updateNetworkStatus(NetworkStatus.FAILED)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                updateNetworkStatus(NetworkStatus.FAILED)
            }
        }
    }

    private fun callLatestNewsAsync(pageNumber: Int): Deferred<Response<NewsResponse>> = treasureApi.getLatestNewsAsync(
        NEWS_BASE_URL, SEARCH_KEY_WORD, SEARCH_NEWS_API_KEY, pageNumber, INITIAL_PAGE_SIZE
    )

    private suspend fun updateNetworkStatus(networkNetworkStatus: NetworkStatus) {
        withContext(Dispatchers.Main) {
            this@NewsDataSource.networkNetworkStatus.value = networkNetworkStatus
        }
    }

    fun getNetworkStatus(): MutableLiveData<NetworkStatus> = networkNetworkStatus

    fun getInitialState(): MutableLiveData<InitialState> = initialState
}