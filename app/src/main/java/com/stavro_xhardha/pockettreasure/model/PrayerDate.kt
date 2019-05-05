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

data class HijriMonth(
    @SerializedName("number")
    val number: Int,

    @SerializedName("en")
    val monthNameInEnglish: String
)

data class GregorianMonth(
    @SerializedName("number")
    val number: Int,

    @SerializedName("en")
    val monthNameInEnglish: String
)