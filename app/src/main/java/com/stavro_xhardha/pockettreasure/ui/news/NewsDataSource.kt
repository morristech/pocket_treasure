package com.stavro_xhardha.pockettreasure.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.model.News
import com.stavro_xhardha.pockettreasure.model.NewsResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsDataSource @Inject constructor(val treasureApi: TreasureApi) : PageKeyedDataSource<Int, News>() {
    private val networkCurrentNetworkStatus: MutableLiveData<CurrentNetworkStatus> = MutableLiveData()
    private val initialNetworkState: MutableLiveData<InitialNetworkState> = MutableLiveData()

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(CurrentNetworkStatus.LOADING)
                updateInitialState(InitialNetworkState.LOADING)
                val primaryNewsResponse = callLatestNewsAsync(1)
                if (primaryNewsResponse.isSuccessful) {
                    callback.onResult(primaryNewsResponse.body()!!.articles, null, 2)
                    updateNetworkStatus(CurrentNetworkStatus.SUCCESS)
                    updateInitialState(InitialNetworkState.SUCCESS)
                } else {
                    updateInitialState(InitialNetworkState.ERROR)
                }
            } catch (e: Exception) {
                updateInitialState(InitialNetworkState.ERROR)
                e.printStackTrace()
            }
        }
    }

    private suspend fun updateInitialState(networkStatus: InitialNetworkState) {
        withContext(Dispatchers.Main) {
            initialNetworkState.value = networkStatus
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                updateNetworkStatus(CurrentNetworkStatus.LOADING)
                val primaryNewsResponse = callLatestNewsAsync(params.key)
                if (primaryNewsResponse.isSuccessful) {
                    callback.onResult(primaryNewsResponse.body()!!.articles, params.key.inc())
                    updateNetworkStatus(CurrentNetworkStatus.SUCCESS)
                } else {
                    updateNetworkStatus(CurrentNetworkStatus.FAILED)
                }
            } catch (e: Exception) {
                updateNetworkStatus(CurrentNetworkStatus.FAILED)
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
    }

    private suspend fun callLatestNewsAsync(pageNumber: Int): Response<NewsResponse> = treasureApi.getLatestNewsAsync(
        NEWS_BASE_URL, SEARCH_KEY_WORD, SEARCH_NEWS_API_KEY, pageNumber, INITIAL_PAGE_SIZE
    )

    private suspend fun updateNetworkStatus(networkCurrentNetworkStatus: CurrentNetworkStatus) {
        withContext(Dispatchers.Main) {
            this@NewsDataSource.networkCurrentNetworkStatus.value = networkCurrentNetworkStatus
        }
    }

    fun getNetworkStatus(): MutableLiveData<CurrentNetworkStatus> = networkCurrentNetworkStatus

    fun getInitialState(): MutableLiveData<InitialNetworkState> = initialNetworkState
}