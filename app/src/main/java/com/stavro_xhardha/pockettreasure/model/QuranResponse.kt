package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class QuranResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: SurahResponse
)