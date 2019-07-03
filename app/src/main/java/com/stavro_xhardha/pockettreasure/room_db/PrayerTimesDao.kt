package com.stavro_xhardha.pockettreasure.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stavro_xhardha.pockettreasure.model.PrayerTiming

@Dao
interface PrayerTimesDao {
    @Query(" SELECT * FROM prayer_times WHERE timestamp = :timeStamp AND is_fired = :isFired")
    suspend fun selectAllPrayersWhereDateAndIsFired(timeStamp: Long, isFired: Int): List<PrayerTiming>

    @Query(" UPDATE prayer_times SET is_fired = :isFired WHERE timestamp = :timeStamp")
    suspend fun updatePrayersWhehereDateAndIsFired(timeStamp: Long, isFired: Boolean)

    @Insert
    suspend fun insertPrayerTimes(prayerTime: PrayerTiming)
}