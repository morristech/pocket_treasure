package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class UnsplashResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("results")
    val results: List<UnsplashResult>
)