package com.stavro_xhardha.pockettreasure.model

data class Tasbeeh(
    val arabicPhrase: String,
    val transliteration: String,
    val translation: String,
    val numberOfTimesPressed: Int = 0
)