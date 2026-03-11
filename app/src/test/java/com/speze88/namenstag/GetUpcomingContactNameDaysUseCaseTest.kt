package com.speze88.namenstag

import com.speze88.namenstag.domain.model.Contact
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.MatchType
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.Saint
import com.speze88.namenstag.domain.usecase.findUpcomingContactNameDays
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class GetUpcomingContactNameDaysUseCaseTest {

    private val saintMartin = Saint(
        id = 1,
        canonicalName = "Martin",
        description = "Test",
        bornYear = null,
        diedYear = null,
        patronOf = null,
    )
    private val saintJohannes = Saint(
        id = 2,
        canonicalName = "Johannes",
        description = "Test",
        bornYear = null,
        diedYear = null,
        patronOf = null,
    )

    @Test
    fun `returns only contacts on nearest upcoming date`() {
        val result = findUpcomingContactNameDays(
            contactNameDays = listOf(
                contactNameDay(
                    id = "1",
                    displayName = "Martin Mueller",
                    givenName = "Martin",
                    nameDays = listOf(NameDay("Martin", 11, 11, saintMartin)),
                ),
                contactNameDay(
                    id = "2",
                    displayName = "Johannes Schmidt",
                    givenName = "Johannes",
                    nameDays = listOf(NameDay("Johannes", 6, 24, saintJohannes)),
                ),
                contactNameDay(
                    id = "3",
                    displayName = "Noch Ein Martin",
                    givenName = "Martin",
                    nameDays = listOf(NameDay("Martin", 11, 11, saintMartin)),
                ),
            ),
            today = LocalDate.of(2026, 6, 1),
        )

        assertEquals(1, result.size)
        assertEquals("Johannes Schmidt", result.first().contactNameDay.contact.displayName)
        assertEquals(LocalDate.of(2026, 6, 24), result.first().date)
    }

    @Test
    fun `rolls over to next year and keeps same nearest date ties`() {
        val result = findUpcomingContactNameDays(
            contactNameDays = listOf(
                contactNameDay(
                    id = "1",
                    displayName = "Anna Beispiel",
                    givenName = "Anna",
                    nameDays = listOf(NameDay("Anna", 1, 5, saintMartin)),
                ),
                contactNameDay(
                    id = "2",
                    displayName = "Anni Beispiel",
                    givenName = "Anni",
                    nameDays = listOf(NameDay("Anna", 1, 5, saintMartin)),
                ),
                contactNameDay(
                    id = "3",
                    displayName = "Johannes Schmidt",
                    givenName = "Johannes",
                    nameDays = listOf(NameDay("Johannes", 2, 1, saintJohannes)),
                ),
            ),
            today = LocalDate.of(2026, 12, 31),
        )

        assertEquals(2, result.size)
        assertEquals(LocalDate.of(2027, 1, 5), result.first().date)
        assertEquals(
            listOf("Anna Beispiel", "Anni Beispiel"),
            result.map { it.contactNameDay.contact.displayName },
        )
    }
}

private fun contactNameDay(
    id: String,
    displayName: String,
    givenName: String,
    nameDays: List<NameDay>,
) = ContactNameDay(
    contact = Contact(
        id = id,
        displayName = displayName,
        givenName = givenName,
    ),
    nameDays = nameDays,
    matchType = MatchType.EXACT,
)
