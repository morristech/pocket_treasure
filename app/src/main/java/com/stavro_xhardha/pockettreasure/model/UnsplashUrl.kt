package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class UnsplashUrl(
    @SerializedName("raw")
    val raw: String,
    @SerializedName("full")
    val fullUrl: String,
    @SerializedName("regular")
    val regularUrl: String,
    @SerializedName("thumb")
    val thumbnailUrl: String
)