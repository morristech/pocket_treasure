package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class PrayerDate(
    @SerializedName("readable")
    val readableDate: String,

    @SerializedName("timestamp")
    val timestamp: String,

    @SerializedName("hijri")
    val hijriPrayerDate: HijriDate,

    @SerializedName("gregorian")
    val gregorianDate: GregorianDate

)