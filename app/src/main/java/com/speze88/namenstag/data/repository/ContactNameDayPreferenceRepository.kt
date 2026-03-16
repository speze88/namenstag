package com.speze88.namenstag.data.repository

import com.speze88.namenstag.domain.model.ContactNameDayPreference
import com.speze88.namenstag.domain.model.NameDay

interface ContactNameDayPreferenceRepository {
    suspend fun getAllPreferences(): Map<String, ContactNameDayPreference>
    suspend fun setPreferredNameDay(contactId: String, nameDay: NameDay)
    suspend fun clearPreferredNameDay(contactId: String)
}
