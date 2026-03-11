package com.speze88.namenstag.domain.model

import java.time.LocalDate

data class UpcomingContactNameDay(
    val contactNameDay: ContactNameDay,
    val nextNameDay: NameDay,
    val date: LocalDate,
    val daysUntil: Long,
)
