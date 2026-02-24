package com.speze88.namenstag

import com.speze88.namenstag.data.repository.NameDayRepository
import com.speze88.namenstag.domain.model.Contact
import com.speze88.namenstag.domain.model.MatchType
import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.Saint
import com.speze88.namenstag.domain.usecase.MatchContactNamesUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class MatchContactNamesUseCaseTest {

    private lateinit var useCase: MatchContactNamesUseCase
    private lateinit var fakeRepository: FakeNameDayRepository

    private val saintJohannes = Saint(
        id = 1, canonicalName = "Johannes",
        description = "Test", bornYear = null, diedYear = null, patronOf = null,
    )
    private val saintMartin = Saint(
        id = 2, canonicalName = "Martin",
        description = "Test", bornYear = null, diedYear = null, patronOf = null,
    )

    private val nameDays = listOf(
        NameDay("Johannes", 6, 24, saintJohannes),
        NameDay("Martin", 11, 11, saintMartin),
    )

    @Before
    fun setup() {
        fakeRepository = FakeNameDayRepository()
        useCase = MatchContactNamesUseCase(fakeRepository)
    }

    @Test
    fun `exact match returns EXACT type`() = runTest {
        val contacts = listOf(
            Contact("1", "Martin Müller", "Martin"),
        )
        val result = useCase(contacts, nameDays)
        assertEquals(1, result.size)
        assertEquals(MatchType.EXACT, result[0].matchType)
        assertEquals("Martin", result[0].nameDays[0].name)
    }

    @Test
    fun `alias match returns ALIAS type`() = runTest {
        fakeRepository.aliases["Hans"] = "Johannes"
        val contacts = listOf(
            Contact("1", "Hans Schmidt", "Hans"),
        )
        val result = useCase(contacts, nameDays)
        assertEquals(1, result.size)
        assertEquals(MatchType.ALIAS, result[0].matchType)
        assertEquals("Johannes", result[0].nameDays[0].name)
    }

    @Test
    fun `no match returns empty list`() = runTest {
        val contacts = listOf(
            Contact("1", "Xylophon Test", "Xylophon"),
        )
        val result = useCase(contacts, nameDays)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `blank given name is skipped`() = runTest {
        val contacts = listOf(
            Contact("1", "Unknown", "  "),
        )
        val result = useCase(contacts, nameDays)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `case insensitive exact match`() = runTest {
        val contacts = listOf(
            Contact("1", "martin klein", "martin"),
        )
        val result = useCase(contacts, nameDays)
        assertEquals(1, result.size)
        assertEquals(MatchType.EXACT, result[0].matchType)
    }
}

private class FakeNameDayRepository : NameDayRepository {
    val aliases = mutableMapOf<String, String>()

    override fun getNameDaysByDate(month: Int, day: Int) = flowOf(emptyList<NameDay>())
    override fun getNameDaysByName(name: String) = flowOf(emptyList<NameDay>())
    override fun searchNameDays(query: String) = flowOf(emptyList<NameDay>())
    override fun getSaintById(id: Long) = flowOf<Saint?>(null)
    override suspend fun getAllNameDays() = emptyList<NameDay>()
    override suspend fun getCanonicalName(alias: String) = aliases[alias]
    override suspend fun getAllCanonicalNames() = emptyList<String>()
    override suspend fun seedDatabaseIfEmpty() {}
}
