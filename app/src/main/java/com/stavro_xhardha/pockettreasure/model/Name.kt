package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class Name(
    @SerializedName("name")
    val arabicName: String,

    @SerializedName("transliteration")
    val transliteration: String,

    @SerializedName("number")
    val number: Int,

    @SerializedName("en")
    val englishNameMeaning: EnglishNameMeaning
)

data class NameResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: ArrayList<Name>
)