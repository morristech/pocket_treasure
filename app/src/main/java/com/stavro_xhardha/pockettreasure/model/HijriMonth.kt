package com.stavro_xhardha.pockettreasure.model

import com.google.gson.annotations.SerializedName

data class HijriMonth(
    @SerializedName("number")
    val number: Int,

    @SerializedName("en")
    val monthNameInEnglish: String
)