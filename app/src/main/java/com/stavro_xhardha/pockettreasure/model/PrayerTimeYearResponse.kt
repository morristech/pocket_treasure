package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName


data class PrayerTimeYearResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: PrayerYear
)

data class PrayerYear(
    @SerializedName("1")
    val january: List<PrayerMonthDays>,
    @SerializedName("2")
    val february: List<PrayerMonthDays>,
    @SerializedName("3")
    val march: List<PrayerMonthDays>,
    @SerializedName("4")
    val april: List<PrayerMonthDays>,
    @SerializedName("5")
    val may: List<PrayerMonthDays>,
    @SerializedName("6")
    val june: List<PrayerMonthDays>,
    @SerializedName("7")
    val july: List<PrayerMonthDays>,
    @SerializedName("8")
    val august: List<PrayerMonthDays>,
    @SerializedName("9")
    val september: List<PrayerMonthDays>,
    @SerializedName("10")
    val october: List<PrayerMonthDays>,
    @SerializedName("11")
    val november: List<PrayerMonthDays>,
    @SerializedName("12")
    val december: List<PrayerMonthDays>
)

data class PrayerMonthDays(
    @SerializedName("timings")
    val timing: PrayerTiming,
    @SerializedName("date")
    val prayerDate: PrayerDate
)