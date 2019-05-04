package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class Country(
    @SerializedName("name")
    val name: String,
    @SerializedName("capital")
    val capitalCity: String,
    @SerializedName("flag")
    val flagUrl: String
)