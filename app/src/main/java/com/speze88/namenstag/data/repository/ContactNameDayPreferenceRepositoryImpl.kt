package com.speze88.namenstag.data.repository

import com.speze88.namenstag.data.local.dao.ContactNameDayPreferenceDao
import com.speze88.namenstag.data.local.entity.ContactNameDayPreferenceEntity
import com.speze88.namenstag.domain.model.ContactNameDayPreference
import com.speze88.namenstag.domain.model.NameDay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactNameDayPreferenceRepositoryImpl @Inject constructor(
    private val dao: ContactNameDayPreferenceDao,
) : ContactNameDayPreferenceRepository {

    override suspend fun getAllPreferences(): Map<String, ContactNameDayPreference> {
        return dao.getAll().associate { entity ->
            entity.contactId to ContactNameDayPreference(
                contactId = entity.contactId,
                saintId = entity.saintId,
                month = entity.month,
                day = entity.day,
            )
        }
    }

    override suspend fun setPreferredNameDay(contactId: String, nameDay: NameDay) {
        dao.upsert(
            ContactNameDayPreferenceEntity(
                contactId = contactId,
                saintId = nameDay.saint.id,
                month = nameDay.month,
                day = nameDay.day,
            ),
        )
    }

    override suspend fun clearPreferredNameDay(contactId: String) {
        dao.deleteByContactId(contactId)
    }
}
