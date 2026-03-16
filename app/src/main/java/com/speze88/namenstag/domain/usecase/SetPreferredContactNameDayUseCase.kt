package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.ContactNameDayPreferenceRepository
import com.speze88.namenstag.domain.model.NameDay
import javax.inject.Inject

class SetPreferredContactNameDayUseCase @Inject constructor(
    private val repository: ContactNameDayPreferenceRepository,
) {
    suspend operator fun invoke(contactId: String, selectedNameDay: NameDay?, availableNameDays: List<NameDay>) {
        val shouldPersist = selectedNameDay != null && availableNameDays.size > 1

        if (shouldPersist) {
            repository.setPreferredNameDay(contactId, selectedNameDay)
        } else {
            repository.clearPreferredNameDay(contactId)
        }
    }
}
