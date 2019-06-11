package com.stavro_xhardha.pockettreasure.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "surahs")
data class Surah(
    @SerializedName("number")
    @PrimaryKey
    @ColumnInfo(name = "surah_number")
    var surahNumber: Int,

    @SerializedName("name")
    @ColumnInfo(name = "surah_arabic_name")
    var surahArabicName: String,

    @SerializedName("englishName")
    @ColumnInfo(name = "surah_english_name")
    var englishName: String,

    @SerializedName("englishNameTranslation")
    @ColumnInfo(name = "surah_english_translation")
    var englishTranslation: String,

    @SerializedName("revelationType")
    @ColumnInfo(name = "surah_relevation_type")
    var revelationType: String,

    @SerializedName("ayahs")
    @Ignore
    var ayas: List<Aya>
) {
    constructor() : this(0, "", "", "", "", listOf())
}