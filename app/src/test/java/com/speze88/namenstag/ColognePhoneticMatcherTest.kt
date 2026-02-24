package com.speze88.namenstag

import com.speze88.namenstag.domain.matching.ColognePhoneticMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ColognePhoneticMatcherTest {

    @Test
    fun `encode empty string returns empty`() {
        assertEquals("", ColognePhoneticMatcher.encode(""))
        assertEquals("", ColognePhoneticMatcher.encode("  "))
    }

    @Test
    fun `Katharina and Catarina produce same code`() {
        assertTrue(ColognePhoneticMatcher.matches("Katharina", "Catarina"))
    }

    @Test
    fun `Stefan and Stephan produce same code`() {
        assertTrue(ColognePhoneticMatcher.matches("Stefan", "Stephan"))
    }

    @Test
    fun `case insensitive matching`() {
        assertTrue(ColognePhoneticMatcher.matches("MARTIN", "martin"))
    }

    @Test
    fun `different names do not match`() {
        assertFalse(ColognePhoneticMatcher.matches("Martin", "Josef"))
    }

    @Test
    fun `umlaut handling`() {
        val code = ColognePhoneticMatcher.encode("Müller")
        assertTrue(code.isNotEmpty())
    }

    @Test
    fun `encode known names produces non-empty codes`() {
        val names = listOf(
            "Johannes", "Maria", "Josef", "Elisabeth", "Katharina",
            "Martin", "Nikolaus", "Andreas", "Michael",
        )
        names.forEach { name ->
            val code = ColognePhoneticMatcher.encode(name)
            assertTrue("Code for $name should not be empty", code.isNotEmpty())
        }
    }

    @Test
    fun `Karl and Carl produce same code`() {
        assertTrue(ColognePhoneticMatcher.matches("Karl", "Carl"))
    }

    @Test
    fun `Christoph and Kristof produce same code`() {
        assertTrue(ColognePhoneticMatcher.matches("Christoph", "Kristof"))
    }
}
