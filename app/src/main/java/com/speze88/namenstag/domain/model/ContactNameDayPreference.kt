package com.speze88.namenstag.domain.model

data class ContactNameDayPreference(
    val contactId: String,
    val saintId: Long,
    val month: Int,
    val day: Int,
)
