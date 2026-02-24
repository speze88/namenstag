package com.speze88.namenstag.domain.model

data class ContactNameDay(
    val contact: Contact,
    val nameDays: List<NameDay>,
    val matchType: MatchType,
)

enum class MatchType {
    EXACT,
    ALIAS,
    PHONETIC,
}
