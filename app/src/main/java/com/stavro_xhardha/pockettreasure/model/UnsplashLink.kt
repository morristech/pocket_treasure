package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class UnsplashLink(
    @SerializedName("self")
    val selfUrl: String,
    @SerializedName("html")
    val htmlUrl: String,
    @SerializedName("download")
    val downloadUrl: String,
    @SerializedName("download_location")
    val downloadLocationUrl: String
)