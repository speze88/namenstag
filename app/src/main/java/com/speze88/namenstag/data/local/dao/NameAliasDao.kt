package com.speze88.namenstag.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speze88.namenstag.data.local.entity.NameAliasEntity

@Dao
interface NameAliasDao {

    @Query("SELECT canonicalName FROM name_aliases WHERE LOWER(alias) = LOWER(:alias)")
    suspend fun getCanonicalName(alias: String): String?

    @Query("SELECT * FROM name_aliases")
    suspend fun getAllAliases(): List<NameAliasEntity>

    @Query("SELECT COUNT(*) FROM name_aliases")
    suspend fun getAliasCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAliases(aliases: List<NameAliasEntity>)
}
