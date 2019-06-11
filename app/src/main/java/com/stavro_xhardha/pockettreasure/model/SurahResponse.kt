package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class SurahResponse(
    @SerializedName("surahs")
    val surahs: List<Surah>
)