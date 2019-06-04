package com.stavro_xhardha.pockettreasure.ui.news

import com.stavro_xhardha.pockettreasure.brain.NEWS_BASE_URL
import com.stavro_xhardha.pockettreasure.brain.SEARCH_KEY_WORD
import com.stavro_xhardha.pockettreasure.model.NewsResponse
import com.stavro_xhardha.pockettreasure.network.TreasureApi
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import kotlinx.coroutines.Deferred
import retrofit2.Response

class NewsRepository(val treasureApi: TreasureApi, val treasureDatabase: TreasureDatabase) {

    fun callLatestNewsAsync(pageNumber: Int): Deferred<Response<NewsResponse>> = treasureApi.getLatestNewsAsync(
        NEWS_BASE_URL, SEARCH_KEY_WORD,
        pageNumber
    )

}