package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class UnsplashUser(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val userFullName: String
)
