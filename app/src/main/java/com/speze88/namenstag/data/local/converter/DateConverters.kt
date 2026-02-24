package com.speze88.namenstag.data.local.converter

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverters {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? = date?.toString()

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? = dateString?.let { LocalDate.parse(it) }
}
