package com.speze88.namenstag.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact_nameday_preferences")
data class ContactNameDayPreferenceEntity(
    @PrimaryKey val contactId: String,
    val saintId: Long,
    val month: Int,
    val day: Int,
)
