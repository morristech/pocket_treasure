package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.sql.Timestamp


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
    val january: List<PrayerMonth>,
    @SerializedName("2")
    val february: List<PrayerMonth>,
    @SerializedName("3")
    val march: List<PrayerMonth>,
    @SerializedName("4")
    val april: List<PrayerMonth>,
    @SerializedName("5")
    val may: List<PrayerMonth>,
    @SerializedName("6")
    val june: List<PrayerMonth>,
    @SerializedName("7")
    val july: List<PrayerMonth>,
    @SerializedName("8")
    val august: List<PrayerMonth>,
    @SerializedName("9")
    val september: List<PrayerMonth>,
    @SerializedName("10")
    val october: List<PrayerMonth>,
    @SerializedName("11")
    val november: List<PrayerMonth>,
    @SerializedName("12")
    val december: List<PrayerMonth>
)

data class PrayerMonth(
    @SerializedName("timings")
    val timing: PrayerTiming,
    @SerializedName("date")
    val prayerDate: PrayerDate
)