package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class PrayerTimeData(
    @SerializedName("timings")
    val timings: PrayerTiming,

    @SerializedName("date")
    val date: PrayerDate
)