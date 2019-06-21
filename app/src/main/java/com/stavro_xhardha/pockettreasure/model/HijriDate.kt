package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class HijriDate(
    @SerializedName("date")
    val date: String,

    @SerializedName("day")
    val day: String,

    @SerializedName("month")
    val hirjiMonth: HijriMonth,

    @SerializedName("year")
    val year: String
)