package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class GregorianDate(
    @SerializedName("date")
    val date: String,

    @SerializedName("day")
    val day: String,

    @SerializedName("month")
    val gregorianMonth: GregorianMonth,

    @SerializedName("year")
    val year: String
)