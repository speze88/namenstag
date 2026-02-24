package com.speze88.namenstag.domain.usecase

import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.matching.ColognePhoneticMatcher
import com.speze88.namenstag.domain.model.Contact
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.MatchType
import com.speze88.namenstag.domain.model.NameDay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MatchContactNamesUseCase @Inject constructor(
    private val repository: NameDayRepository,
) {
    suspend operator fun invoke(
        contacts: List<Contact>,
        nameDays: List<NameDay>,
    ): List<ContactNameDay> {
        val nameDaysByName = nameDays.groupBy { it.name.lowercase() }
        val canonicalNames = nameDaysByName.keys

        return contacts.mapNotNull { contact ->
            val givenName = contact.givenName.trim()
            if (givenName.isBlank()) return@mapNotNull null

            // 1. Exact match (case-insensitive)
            val exactMatch = nameDaysByName[givenName.lowercase()]
            if (exactMatch != null) {
                return@mapNotNull ContactNameDay(contact, exactMatch, MatchType.EXACT)
            }

            // 2. Alias lookup
            val canonical = repository.getCanonicalName(givenName)
            if (canonical != null) {
                val aliasMatch = nameDaysByName[canonical.lowercase()]
                if (aliasMatch != null) {
                    return@mapNotNull ContactNameDay(contact, aliasMatch, MatchType.ALIAS)
                }
            }

            // 3. Cologne Phonetic matching
            val contactCode = ColognePhoneticMatcher.encode(givenName)
            if (contactCode.isNotEmpty()) {
                for (name in canonicalNames) {
                    val nameCode = ColognePhoneticMatcher.encode(name)
                    if (nameCode == contactCode) {
                        val phoneticMatch = nameDaysByName[name]
                        if (phoneticMatch != null) {
                            return@mapNotNull ContactNameDay(
                                contact,
                                phoneticMatch,
                                MatchType.PHONETIC,
                            )
                        }
                    }
                }
            }

            null
        }
    }
}
