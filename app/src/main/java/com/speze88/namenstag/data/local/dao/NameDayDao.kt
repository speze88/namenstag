package com.speze88.namenstag.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speze88.namenstag.data.local.entity.NameDayEntity
import com.speze88.namenstag.data.local.entity.SaintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NameDayDao {

    @Query(
        """
        SELECT * FROM name_days
        WHERE month = :month AND day = :day AND language = :language
        """
    )
    fun getNameDaysByDate(month: Int, day: Int, language: String = "de"): Flow<List<NameDayEntity>>

    @Query(
        """
        SELECT * FROM name_days
        WHERE LOWER(name) = LOWER(:name) AND language = :language
        """
    )
    fun getNameDaysByName(name: String, language: String = "de"): Flow<List<NameDayEntity>>

    @Query("SELECT * FROM saints WHERE id = :id")
    suspend fun getSaintById(id: Long): SaintEntity?

    @Query("SELECT * FROM saints WHERE LOWER(canonicalName) = LOWER(:name) AND language = :language")
    suspend fun getSaintByName(name: String, language: String = "de"): SaintEntity?

    @Query("SELECT * FROM saints WHERE id = :id")
    fun getSaintByIdFlow(id: Long): Flow<SaintEntity?>

    @Query(
        """
        SELECT s.* FROM saints s
        INNER JOIN name_days nd ON s.id = nd.saintId
        WHERE nd.month = :month AND nd.day = :day AND nd.language = :language
        """
    )
    fun getSaintsByDate(month: Int, day: Int, language: String = "de"): Flow<List<SaintEntity>>

    @Query("SELECT * FROM name_days WHERE language = :language")
    suspend fun getAllNameDays(language: String = "de"): List<NameDayEntity>

    @Query(
        """
        SELECT * FROM name_days
        WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' AND language = :language
        """
    )
    fun searchNameDays(query: String, language: String = "de"): Flow<List<NameDayEntity>>

    @Query("SELECT COUNT(*) FROM name_days")
    suspend fun getNameDayCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNameDays(nameDays: List<NameDayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaints(saints: List<SaintEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaint(saint: SaintEntity): Long
}
