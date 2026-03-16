package com.speze88.namenstag.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speze88.namenstag.data.local.entity.ContactNameDayPreferenceEntity

@Dao
interface ContactNameDayPreferenceDao {
    @Query("SELECT * FROM contact_nameday_preferences")
    suspend fun getAll(): List<ContactNameDayPreferenceEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(preference: ContactNameDayPreferenceEntity)

    @Query("DELETE FROM contact_nameday_preferences WHERE contactId = :contactId")
    suspend fun deleteByContactId(contactId: String)
}
