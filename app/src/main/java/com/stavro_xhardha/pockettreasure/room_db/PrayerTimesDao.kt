package com.stavro_xhardha.pockettreasure.room_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.stavro_xhardha.pockettreasure.model.PrayerTiming

@Dao
interface PrayerTimesDao {
    @Query(" SELECT * FROM prayer_times WHERE timestamp = :timeStamp AND is_fired = :isFired")
    suspend fun selectAllPrayersWhereDateAndIsFired(timeStamp: Long, isFired: Int): PrayerTiming?

    @Query(" SELECT * FROM prayer_times WHERE timestamp = :timeStamp")
    suspend fun selectAllPrayersWhereDate(timeStamp: Long): List<PrayerTiming>

    @Query(" UPDATE prayer_times SET is_fired = :isFired WHERE timestamp = :timeStamp")
    suspend fun updatePrayersWhehereDate(timeStamp: Long, isFired: Int)

    @Query(" SELECT * FROM prayer_times")
    suspend fun selectAll(): List<PrayerTiming>

    @Insert
    suspend fun insertPrayerTimes(prayerTime: PrayerTiming)

    @Query(" SELECT midnight FROM prayer_times WHERE timestamp = :millis AND is_fired = :fired")
    suspend fun findTodaysMidnight(millis: Long, fired: Int): String

    @Query(" DELETE FROM prayer_times ")
    suspend fun deleteAllDataInside()
}