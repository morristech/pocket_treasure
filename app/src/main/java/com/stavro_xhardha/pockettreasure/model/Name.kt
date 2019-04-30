package com.stavro_xhardha.pockettreasure.model

data class Name(
    val arabicName: String,
    val transliteration: String,
    val number: Int,
    val englishNameMeaning: EnglishNameMeaning
)