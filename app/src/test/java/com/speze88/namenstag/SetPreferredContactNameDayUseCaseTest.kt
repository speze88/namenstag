package com.speze88.namenstag

import com.speze88.namenstag.data.repository.ContactNameDayPreferenceRepository
import com.speze88.namenstag.domain.model.ContactNameDayPreference
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.Saint
import com.speze88.namenstag.domain.usecase.SetPreferredContactNameDayUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class SetPreferredContactNameDayUseCaseTest {

    private lateinit var repository: FakeContactNameDayPreferenceRepository
    private lateinit var useCase: SetPreferredContactNameDayUseCase

    private val saintJohannes = Saint(
        id = 1,
        canonicalName = "Johannes",
        description = "Test",
        bornYear = null,
        diedYear = null,
        patronOf = null,
    )

    @Before
    fun setup() {
        repository = FakeContactNameDayPreferenceRepository()
        useCase = SetPreferredContactNameDayUseCase(repository)
    }

    @Test
    fun `persists preferred name day when multiple options exist`() = runTest {
        val preferredNameDay = NameDay("Johannes", 6, 24, saintJohannes)

        useCase(
            contactId = "42",
            selectedNameDay = preferredNameDay,
            availableNameDays = listOf(
                preferredNameDay,
                NameDay("Johannes", 8, 29, saintJohannes),
            ),
        )

        assertEquals(preferredNameDay.saint.id, repository.preferences["42"]?.saintId)
        assertEquals(preferredNameDay.month, repository.preferences["42"]?.month)
        assertEquals(preferredNameDay.day, repository.preferences["42"]?.day)
    }

    @Test
    fun `clears preference when automatic selection is chosen`() = runTest {
        repository.preferences["42"] = ContactNameDayPreference(
            contactId = "42",
            saintId = saintJohannes.id,
            month = 6,
            day = 24,
        )

        useCase(
            contactId = "42",
            selectedNameDay = null,
            availableNameDays = listOf(
                NameDay("Johannes", 6, 24, saintJohannes),
                NameDay("Johannes", 8, 29, saintJohannes),
            ),
        )

        assertNull(repository.preferences["42"])
    }

    @Test
    fun `clears preference when only one name day exists`() = runTest {
        val onlyNameDay = NameDay("Johannes", 6, 24, saintJohannes)

        useCase(
            contactId = "42",
            selectedNameDay = onlyNameDay,
            availableNameDays = listOf(onlyNameDay),
        )

        assertNull(repository.preferences["42"])
    }
}

private class FakeContactNameDayPreferenceRepository : ContactNameDayPreferenceRepository {
    val preferences = mutableMapOf<String, ContactNameDayPreference>()

    override suspend fun getAllPreferences(): Map<String, ContactNameDayPreference> = preferences

    override suspend fun setPreferredNameDay(contactId: String, nameDay: NameDay) {
        preferences[contactId] = ContactNameDayPreference(
            contactId = contactId,
            saintId = nameDay.saint.id,
            month = nameDay.month,
            day = nameDay.day,
        )
    }

    override suspend fun clearPreferredNameDay(contactId: String) {
        preferences.remove(contactId)
    }
}
