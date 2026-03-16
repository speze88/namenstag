package com.speze88.namenstag.domain.model

data class ContactNameDay(
    val contact: Contact,
    val nameDays: List<NameDay>,
    val matchType: MatchType,
    val preferredNameDay: NameDay? = null,
)

val ContactNameDay.effectiveNameDays: List<NameDay>
    get() = preferredNameDay?.let(::listOf) ?: nameDays

enum class MatchType {
    EXACT,
    ALIAS,
    PHONETIC,
}
