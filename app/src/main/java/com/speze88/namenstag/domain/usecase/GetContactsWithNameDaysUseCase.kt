package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.ContactRepository
import com.speze88.namenstag.data.repository.ContactNameDayPreferenceRepository
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.ContactNameDay
import javax.inject.Inject

class GetContactsWithNameDaysUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
    private val preferenceRepository: ContactNameDayPreferenceRepository,
    private val nameDayRepository: NameDayRepository,
    private val matchContactNames: MatchContactNamesUseCase,
) {
    suspend operator fun invoke(): List<ContactNameDay> {
        val contacts = contactRepository.getContacts()
        val allNameDays = nameDayRepository.getAllNameDays()
        val preferences = preferenceRepository.getAllPreferences()

        return matchContactNames(contacts, allNameDays).map { contactNameDay ->
            val preference = preferences[contactNameDay.contact.id]
            val preferredNameDay = if (preference == null) {
                null
            } else {
                contactNameDay.nameDays.firstOrNull { nameDay ->
                    nameDay.saint.id == preference.saintId &&
                        nameDay.month == preference.month &&
                        nameDay.day == preference.day
                }
            }

            if (preferredNameDay != null) {
                contactNameDay.copy(preferredNameDay = preferredNameDay)
            } else {
                contactNameDay
            }
        }
    }
}
