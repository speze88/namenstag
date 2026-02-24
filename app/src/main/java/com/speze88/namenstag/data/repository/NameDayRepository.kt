package com.speze88.namenstag.data.repository

import com.speze88.namenstag.domain.model.NameDay
import com.speze88.namenstag.domain.model.Saint
import kotlinx.coroutines.flow.Flow

interface NameDayRepository {
    fun getNameDaysByDate(month: Int, day: Int): Flow<List<NameDay>>
    fun getNameDaysByName(name: String): Flow<List<NameDay>>
    fun searchNameDays(query: String): Flow<List<NameDay>>
    fun getSaintById(id: Long): Flow<Saint?>
    suspend fun getAllNameDays(): List<NameDay>
    suspend fun getCanonicalName(alias: String): String?
    suspend fun getAllCanonicalNames(): List<String>
    suspend fun seedDatabaseIfEmpty()
}
