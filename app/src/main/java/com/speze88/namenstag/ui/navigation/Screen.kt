package com.speze88.namenstag.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object TodayRoute

@Serializable
object CalendarRoute

@Serializable
object ContactsRoute

@Serializable
object SearchRoute

@Serializable
data class SaintDetailRoute(val saintId: Long)
