package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class NewsSource(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String
)
