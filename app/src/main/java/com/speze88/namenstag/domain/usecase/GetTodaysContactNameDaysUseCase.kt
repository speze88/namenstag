package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.effectiveNameDays
import java.time.LocalDate
import javax.inject.Inject

class GetTodaysContactNameDaysUseCase @Inject constructor(
    private val getContactsWithNameDays: GetContactsWithNameDaysUseCase,
) {
    suspend operator fun invoke(today: LocalDate = LocalDate.now()): List<ContactNameDay> {
        return getContactsWithNameDays().filter { contactNameDay ->
            contactNameDay.effectiveNameDays.any { nameDay ->
                nameDay.month == today.monthValue && nameDay.day == today.dayOfMonth
            }
        }
    }
}
