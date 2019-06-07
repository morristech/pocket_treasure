package com.stavro_xhardha.pockettreasure.ui.settings

import com.stavro_xhardha.pockettreasure.brain.*
import com.stavro_xhardha.rocket.Rocket
import javax.inject.Inject

class SettingsRepository @Inject constructor(val rocket: Rocket) {

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
}