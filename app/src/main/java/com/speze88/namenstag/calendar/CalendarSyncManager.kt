package com.speze88.namenstag.calendar

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import com.speze88.namenstag.domain.model.ContactNameDay
import com.speze88.namenstag.domain.model.effectiveNameDays
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarSyncManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val ACCOUNT_NAME = "namenstag.local"
        private const val CALENDAR_NAME = "Namenstage"
    }

    fun syncContactNameDays(contactNameDays: List<ContactNameDay>) {
        val calendarId = ensureCalendar() ?: return
        deleteExistingEvents(calendarId)

        contactNameDays.forEach { contactNameDay ->
            contactNameDay.effectiveNameDays.forEach { nameDay ->
                insertRecurringEvent(calendarId, contactNameDay, nameDay)
            }
        }
    }

    private fun ensureCalendar(): Long? {
        val contentResolver = context.contentResolver
        val projection = arrayOf(CalendarContract.Calendars._ID)
        val selection = "${CalendarContract.Calendars.ACCOUNT_NAME} = ? AND ${CalendarContract.Calendars.ACCOUNT_TYPE} = ?"
        val selectionArgs = arrayOf(ACCOUNT_NAME, CalendarContract.ACCOUNT_TYPE_LOCAL)

        contentResolver.query(
            CalendarContract.Calendars.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null,
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getLong(0)
            }
        }

        val calendarValues = ContentValues().apply {
            put(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
            put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
            put(CalendarContract.Calendars.NAME, CALENDAR_NAME)
            put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME)
            put(CalendarContract.Calendars.VISIBLE, 1)
            put(CalendarContract.Calendars.CALENDAR_COLOR, 0xFF2E7D32.toInt())
            put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER)
            put(CalendarContract.Calendars.OWNER_ACCOUNT, ACCOUNT_NAME)
            put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        }

        val insertUri = CalendarContract.Calendars.CONTENT_URI.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL)
            .build()

        return contentResolver.insert(insertUri, calendarValues)?.lastPathSegment?.toLongOrNull()
    }

    private fun deleteExistingEvents(calendarId: Long) {
        context.contentResolver.delete(
            CalendarContract.Events.CONTENT_URI,
            "${CalendarContract.Events.CALENDAR_ID} = ?",
            arrayOf(calendarId.toString()),
        )
    }

    private fun insertRecurringEvent(
        calendarId: Long,
        contactNameDay: ContactNameDay,
        nameDay: com.speze88.namenstag.domain.model.NameDay,
    ) {
        val startDate = java.time.LocalDate.of(2026, nameDay.month, nameDay.day)
        val zoneId = ZoneId.systemDefault()
        val startMillis = startDate
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()
        val endMillis = startDate
            .plusDays(1)
            .atStartOfDay(zoneId)
            .toInstant()
            .toEpochMilli()

        val values = ContentValues().apply {
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.TITLE, "Namenstag: ${contactNameDay.contact.displayName}")
            put(
                CalendarContract.Events.DESCRIPTION,
                "${nameDay.saint.canonicalName} (${nameDay.day}.${nameDay.month}.)",
            )
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.EVENT_TIMEZONE, zoneId.id)
            put(CalendarContract.Events.ALL_DAY, 1)
            put(CalendarContract.Events.RRULE, "FREQ=YEARLY")
            put(CalendarContract.Events.HAS_ALARM, 0)
            put(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED)
        }

        context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }
}
