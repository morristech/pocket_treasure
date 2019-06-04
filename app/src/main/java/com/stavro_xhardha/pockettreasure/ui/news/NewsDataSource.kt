package com.stavro_xhardha.pockettreasure.ui.news

import androidx.paging.PageKeyedDataSource
import com.stavro_xhardha.pockettreasure.model.News
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

class NewsDataSource(val repository: NewsRepository) : PageKeyedDataSource<Int, News>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, News>) {
        GlobalScope.async(Dispatchers.IO) {
            try {
                val primaryNewsResponse = repository.callLatestNewsAsync(1).await()
                if (primaryNewsResponse.isSuccessful)
                    callback.onResult(primaryNewsResponse.body()!!.articles, null, 2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        GlobalScope.async(Dispatchers.IO) {
            try {
                val primaryNewsResponse = repository.callLatestNewsAsync(params.key).await()
                if (primaryNewsResponse.isSuccessful)
                    callback.onResult(primaryNewsResponse.body()!!.articles, params.key.inc())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, News>) {
        GlobalScope.async(Dispatchers.IO) {
            try {
                val primaryNewsResponse = repository.callLatestNewsAsync(params.key).await()
                if (primaryNewsResponse.isSuccessful)
                    callback.onResult(primaryNewsResponse.body()!!.articles, params.key.dec())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}