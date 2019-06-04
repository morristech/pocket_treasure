package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class News(
    @SerializedName("source")
    val newsSource: NewsSource,
    @SerializedName("author")
    val author: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val urlOfImage: String,
    @SerializedName("publishedAt")
    val publishedAt: String,
    @SerializedName("content")
    val content: String
)