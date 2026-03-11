package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.UpcomingContactNameDay
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class GetUpcomingContactNameDaysUseCase @Inject constructor(
    private val getContactsWithNameDays: GetContactsWithNameDaysUseCase,
) {
    suspend operator fun invoke(today: LocalDate = LocalDate.now()): List<UpcomingContactNameDay> {
        return findUpcomingContactNameDays(
            contactNameDays = getContactsWithNameDays(),
            today = today,
        )
    }
}

internal fun findUpcomingContactNameDays(
    contactNameDays: List<ContactNameDay>,
    today: LocalDate,
): List<UpcomingContactNameDay> {
    val upcoming = contactNameDays.mapNotNull { contactNameDay ->
        contactNameDay.nextUpcomingNameDay(today)
    }

    val nearestDate = upcoming.minOfOrNull { it.date } ?: return emptyList()
    return upcoming.filter { it.date == nearestDate }
}

private fun ContactNameDay.nextUpcomingNameDay(today: LocalDate): UpcomingContactNameDay? {
    val nextEntry = nameDays
        .map { nameDay ->
            val date = nameDay.nextOccurrenceAfter(today)
            UpcomingContactNameDay(
                contactNameDay = this,
                nextNameDay = nameDay,
                date = date,
                daysUntil = ChronoUnit.DAYS.between(today, date),
            )
        }
        .minByOrNull { it.date }

    return nextEntry
}

private fun NameDay.nextOccurrenceAfter(today: LocalDate): LocalDate {
    val thisYear = today.withMonth(month).withDayOfMonth(day)
    return if (thisYear.isAfter(today)) {
        thisYear
    } else {
        thisYear.plusYears(1)
    }
}
