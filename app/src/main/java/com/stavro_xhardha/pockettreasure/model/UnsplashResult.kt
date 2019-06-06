package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class UnsplashResult(
    @SerializedName("id")
    val id: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("alt_description")
    val altDescription: String?,
    @SerializedName("urls")
    val photoUrls: UnsplashUrl,
    @SerializedName("links")
    val links: UnsplashLink,
    @SerializedName("user")
    val user: UnsplashUser
)