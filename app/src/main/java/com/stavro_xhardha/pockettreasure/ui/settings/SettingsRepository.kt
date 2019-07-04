package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.pockettreasure.room_db.PrayerTimesDao
import com.stavro_xhardha.pockettreasure.room_db.TreasureDatabase
import com.stavro_xhardha.rocket.Rocket
import javax.inject.Inject

class SettingsRepository @Inject constructor(val rocket: Rocket, val prayerTimesDao: PrayerTimesDao) {

    fun getFajrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_FAJR)

    fun getDhuhrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_DHUHR)

    fun getAsrChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ASR)

    fun getMaghribChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_MAGHRIB)

    fun getIshaChecked(): Boolean = rocket.readBoolean(NOTIFY_USER_FOR_ISHA)

    fun putFajrNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_FAJR, oppositeValue)
    }

    fun putAsrNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_ASR, oppositeValue)
    }

    fun putMaghribNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_MAGHRIB, oppositeValue)
    }

    fun putIshaNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_ISHA, oppositeValue)
    }

    fun putDhuhrNotification(oppositeValue: Boolean) {
        rocket.writeBoolean(NOTIFY_USER_FOR_DHUHR, oppositeValue)
    }

    fun readCountryAndCapital(): String? = "${rocket.readString(CAPITAL_SHARED_PREFERENCES_KEY)} , ${rocket.readString(
        COUNTRY_SHARED_PREFERENCE_KEY
    )}"

    suspend fun deleteAllDataInside() {
        prayerTimesDao.deleteAllDataInside()
    }
}