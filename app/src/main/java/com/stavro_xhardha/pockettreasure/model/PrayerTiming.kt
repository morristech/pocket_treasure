package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "prayer_times"
)
class PrayerTiming(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,

    @SerializedName("Fajr")
    @ColumnInfo(name = "fajr")
    val fajr: String,

    @SerializedName("Sunrise")
    @ColumnInfo(name = "sunrinse")
    val sunrise: String,

    @SerializedName("Dhuhr")
    @ColumnInfo(name = "dhuhr")
    val dhuhr: String,

    @SerializedName("Asr")
    @ColumnInfo(name = "asr")
    val asr: String,

    @SerializedName("Sunset")
    @ColumnInfo(name = "sunset")
    val sunset: String,

    @SerializedName("Maghrib")
    @ColumnInfo(name = "maghrib")
    val magrib: String,

    @SerializedName("Isha")
    @ColumnInfo(name = "isha")
    val isha: String,

    @SerializedName("Imsak")
    @ColumnInfo(name = "imsak")
    val imsak: String,

    @SerializedName("Midnight")
    @ColumnInfo(name = "midnight")
    val midnight: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "is_fired")
    val isFired: Int
)