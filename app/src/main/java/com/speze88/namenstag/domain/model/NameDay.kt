package com.speze88.namenstag.domain.model

import java.time.LocalDate
import java.time.MonthDay

data class NameDay(
    val name: String,
    val month: Int,
    val day: Int,
    val saint: Saint,
) {
    val monthDay: MonthDay get() = MonthDay.of(month, day)

    fun isToday(): Boolean {
        val today = LocalDate.now()
        return today.monthValue == month && today.dayOfMonth == day
    }
}
