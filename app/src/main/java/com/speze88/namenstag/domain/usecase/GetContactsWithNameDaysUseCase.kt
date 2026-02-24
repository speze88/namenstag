package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.ContactRepository
import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.ContactNameDay
import javax.inject.Inject

class GetContactsWithNameDaysUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
    private val nameDayRepository: NameDayRepository,
    private val matchContactNames: MatchContactNamesUseCase,
) {
    suspend operator fun invoke(): List<ContactNameDay> {
        val contacts = contactRepository.getContacts()
        val allNameDays = nameDayRepository.getAllNameDays()
        return matchContactNames(contacts, allNameDays)
    }
}
